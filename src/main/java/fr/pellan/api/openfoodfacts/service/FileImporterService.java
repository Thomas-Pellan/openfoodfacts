package fr.pellan.api.openfoodfacts.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import fr.pellan.api.openfoodfacts.config.OpenFoodApiConfig;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsArticleEntity;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsIngredientEntity;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsNutrientLevelsEntity;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsArticleRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsIngredientsRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsNutrientLevelsRepository;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsArticleDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.events.OpenFoodFactsFileImportEvent;
import fr.pellan.api.openfoodfacts.exception.OpenFoodFactsFileImportException;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsArticleEntityFactory;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsIngredientEntityFactory;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsNutrientLevelsEntityFactory;
import fr.pellan.api.openfoodfacts.util.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Slf4j
@Service
public class FileImporterService {

    @Autowired
    private OpenFoodApiConfig openFoodApiConfig;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private OpenFoodFactsFileRepository openFoodFactsFileRepository;

    @Autowired
    private OpenFoodFactsArticleEntityFactory openFoodFactsArticleEntityFactory;

    @Autowired
    private OpenFoodFactsArticleRepository openFoodFactsArticleRepository;

    @Autowired
    private OpenFoodFactsIngredientsRepository openFoodFactsIngredientsRepository;

    @Autowired
    private OpenFoodFactsIngredientEntityFactory openFoodFactsIngredientEntityFactory;

    @Autowired
    private OpenFoodFactsNutrientLevelsRepository openFoodFactsNutrientLevelsRepository;

    @Autowired
    private OpenFoodFactsNutrientLevelsEntityFactory openFoodFactsNutrientLevelsEntityFactory;

    @EventListener
    public void importFileEventListener(OpenFoodFactsFileImportEvent event){

        try {
            event.getFile().setFileStatus(OpenFoodFactsFileStatus.IMPORT_STARTED);
            openFoodFactsFileRepository.save(event.getFile());

            importFile(event.getFile());

        } catch(OpenFoodFactsFileImportException e) {

            log.error(e.getMessage(), e);

            event.getFile().setFileStatus(OpenFoodFactsFileStatus.IMPORT_FAILED);
            openFoodFactsFileRepository.save(event.getFile());
            return;
        }

        event.getFile().setFileStatus(OpenFoodFactsFileStatus.IMPORT_FINISHED);
        openFoodFactsFileRepository.save(event.getFile());
    }

    private void importFile(OpenFoodFactsFileEntity file) throws OpenFoodFactsFileImportException {

        if(file == null || StringUtils.isBlank(file.getFileName())){
            throw new OpenFoodFactsFileImportException("empty file or filename, nothimg to query");
        }

        //Get the file, unzip it and get it's lines
        List<String> fileContentList = new ArrayList<>();
        try (InputStream is = new GZIPInputStream(queryUtil.getFileData(openFoodApiConfig.getStaticDataFilesUrl()+ file.getFileName())))
        {
            String fileContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            fileContentList = Arrays.asList(fileContent.split(openFoodApiConfig.getFileSeparator()));

            if(CollectionUtils.isEmpty(fileContentList)){
                log.warn("{} : empty file", file.getFileName());
                return;
            }
        } catch (IOException e) {
            log.error("importFile : error while getting {} content, data not imported", file.getFileName(), e);
            throw new OpenFoodFactsFileImportException("error during file query or unzip");
        }

        fileContentList.stream().forEach(d -> importOpenFoodFactsData(d));
    }

    private boolean importOpenFoodFactsData(String strData){

        Gson gson = new GsonBuilder().create();
        OpenFoodFactsArticleDTO articleDto;
        try
        {
            articleDto = gson.fromJson(strData, OpenFoodFactsArticleDTO.class);
        }
        catch(JsonSyntaxException e){
            log.warn("importOpenFoodFactsData : error while parsing json {}", strData, e);
            return false;
        }

        OpenFoodFactsArticleEntity savedArticle = importOpenFoodFactsDataArticle(articleDto);
        if(savedArticle == null){
            return false;
        }

        importOpenFoodFactsDataNutrients(articleDto, savedArticle);

        importOpenFoodFactsDataIngredients(articleDto, savedArticle);

        return true;
    }

    private OpenFoodFactsArticleEntity importOpenFoodFactsDataArticle(OpenFoodFactsArticleDTO articleDto){

        OpenFoodFactsArticleEntity article = openFoodFactsArticleRepository.findByOpenFFId(articleDto.getId());
        article = openFoodFactsArticleEntityFactory.buildOrMergeArticle(articleDto, article);
        try {

            return openFoodFactsArticleRepository.save(article);
        } catch (DataAccessException e) {
            log.debug("importOpenFoodFactsDataArticle : error on article with id {}", articleDto.getId());
            return null;
        }
    }

    private boolean importOpenFoodFactsDataNutrients(OpenFoodFactsArticleDTO articleDto, OpenFoodFactsArticleEntity article) {

        if(articleDto.getNutrientLevels() == null){
           return true;
        }

        OpenFoodFactsNutrientLevelsEntity nutrients = openFoodFactsNutrientLevelsRepository.findByArticleId(article.getId());
        nutrients = openFoodFactsNutrientLevelsEntityFactory.buildOrMergeNutrient(articleDto.getNutrientLevels(), nutrients);
        nutrients.setArticle(article);
        try {

            openFoodFactsNutrientLevelsRepository.save(nutrients);
        } catch (DataAccessException e) {
            log.warn("importOpenFoodFactsDataNutrients : error saving data", e);
        }

        return true;
    }

    private boolean importOpenFoodFactsDataIngredients(OpenFoodFactsArticleDTO articleDto, OpenFoodFactsArticleEntity article){

        if(CollectionUtils.isEmpty(articleDto.getIngredients()) || article == null){
            return true;
        }

        List<OpenFoodFactsIngredientEntity> ingredients = new ArrayList<>();

        articleDto.getIngredients().stream().forEach(i -> {
            OpenFoodFactsIngredientEntity ingredient = openFoodFactsIngredientsRepository.findByOpenFFId(i.getId());
            ingredient = openFoodFactsIngredientEntityFactory.buildOrMergeIngredient(i, ingredient);
            ingredient.setArticle(article);
            ingredients.add(ingredient);
        });

        //Remove duplicated ids
        HashSet<Object> seen=new HashSet<>();
        ingredients.removeIf(e->!seen.add(e.getId()));

        try {

            openFoodFactsIngredientsRepository.saveAll(ingredients);
        } catch (DataAccessException e) {
            log.warn("importOpenFoodFactsDataIngredients : error saving data", e);
        }

        return false;
    }
}
