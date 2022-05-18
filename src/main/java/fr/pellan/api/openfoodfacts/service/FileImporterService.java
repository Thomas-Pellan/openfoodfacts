package fr.pellan.api.openfoodfacts.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import fr.pellan.api.openfoodfacts.config.OpenFoodApiConfig;
import fr.pellan.api.openfoodfacts.config.OpenFoodBusinessConfig;
import fr.pellan.api.openfoodfacts.db.entity.*;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsArticleRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileImportRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsIngredientsRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsNutrientLevelsRepository;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsArticleDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.events.OpenFoodFactsFileImportEvent;
import fr.pellan.api.openfoodfacts.exception.OpenFoodFactsFileImportException;
import fr.pellan.api.openfoodfacts.exception.QueryUtilException;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsArticleEntityFactory;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsFileImportEntityFactory;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsIngredientEntityFactory;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsNutrientLevelsEntityFactory;
import fr.pellan.api.openfoodfacts.util.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
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

/**
 * This class handles the logic to import one single open food facts file data into the database,
 * and updating the import process during it.
 */
@Slf4j
@Service
public class FileImporterService {

    @Autowired
    private OpenFoodApiConfig openFoodApiConfig;

    @Autowired
    private OpenFoodBusinessConfig openFoodBusinessConfig;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private FileImportService fileImportService;

    @Autowired
    private OpenFoodFactsArticleEntityFactory openFoodFactsArticleEntityFactory;

    @Autowired
    private OpenFoodFactsArticleRepository openFoodFactsArticleRepository;

    @Autowired
    private OpenFoodFactsFileImportRepository openFoodFactsFileImportRepository;

    @Autowired
    private OpenFoodFactsIngredientsRepository openFoodFactsIngredientsRepository;

    @Autowired
    private OpenFoodFactsIngredientEntityFactory openFoodFactsIngredientEntityFactory;

    @Autowired
    private OpenFoodFactsNutrientLevelsRepository openFoodFactsNutrientLevelsRepository;

    @Autowired
    private OpenFoodFactsNutrientLevelsEntityFactory openFoodFactsNutrientLevelsEntityFactory;

    @Autowired
    private OpenFoodFactsFileImportEntityFactory openFoodFactsFileImportEntityFactory;

    /**
     * Async event, used to trigger a file import using file data.
     * @param event the data of the file to import.
     */
    @Async
    @EventListener
    public void importFileEventListener(OpenFoodFactsFileImportEvent event){

        OpenFoodFactsFileStatus status;
        OpenFoodFactsFileImportEntity importData = openFoodFactsFileImportEntityFactory.buildImportData(event.getFile());

        importData = fileImportService.startImport(event.getFile(), importData);

        try {

            status = importFile(event.getFile(), importData);

        } catch(OpenFoodFactsFileImportException e) {

            log.error(e.getMessage(), e);
            fileImportService.endImport(event.getFile(), importData, OpenFoodFactsFileStatus.IMPORT_FAILED);
            return;
        }

        fileImportService.endImport(event.getFile(), importData, status);
    }

    /**
     * Imports a file from the open food fact API into the local database.
     * @param file the file to import
     * @param importData the import process data
     * @return a file status describing how the import process ended
     * @throws OpenFoodFactsFileImportException in case the data isn't correct or import fails
     */
    private OpenFoodFactsFileStatus importFile(OpenFoodFactsFileEntity file, OpenFoodFactsFileImportEntity importData) throws OpenFoodFactsFileImportException {

        if(file == null || StringUtils.isBlank(file.getFileName())){
            throw new OpenFoodFactsFileImportException("empty file or filename, nothing to query");
        }

        //Get the file, unzip it and get it's lines
        List<String> fileContentList;
        try (InputStream is = new GZIPInputStream(queryUtil.getFileData(openFoodApiConfig.getStaticDataFilesUrl()+ file.getFileName())))
        {
            String fileContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            fileContentList = Arrays.asList(fileContent.split(openFoodApiConfig.getFileSeparator()));

            importData.setNbLines(fileContentList.size());
            openFoodFactsFileImportRepository.save(importData);

            if(CollectionUtils.isEmpty(fileContentList)){
                log.warn("{} : empty file", file.getFileName());
                return OpenFoodFactsFileStatus.IMPORT_FILE_EMPTY;
            }
        } catch (IOException e) {

            log.error("importFile : error while getting {} content, data not imported", file.getFileName(), e);
            throw new OpenFoodFactsFileImportException("error during file query or unzip");
        } catch (QueryUtilException e) {

            log.warn("importFile : file {} was unreachable (last query date : {})", file.getFileName(), file.getFileQueryTime().toString());
            return OpenFoodFactsFileStatus.IMPORT_FILE_UNREACHABLE;
        }

        fileContentList.stream().forEach(d -> importOpenFoodFactsData(d, importData));

        return OpenFoodFactsFileStatus.IMPORT_FINISHED;
    }

    /**
     * Extracts the openfoodfacts data out of a file line.
     * @param strData a file line containing openfoodfacts article
     * @param importData the import process data
     */
    private void importOpenFoodFactsData(String strData, OpenFoodFactsFileImportEntity importData){

        Gson gson = new GsonBuilder().create();
        OpenFoodFactsArticleDTO articleDto;
        try
        {
            articleDto = gson.fromJson(strData, OpenFoodFactsArticleDTO.class);
        }
        catch(JsonSyntaxException e){
            log.warn("importOpenFoodFactsData : error while parsing json {}", strData, e);
            fileImportService.incrementNbLinesImported(importData);
            return;
        }

        OpenFoodFactsArticleEntity savedArticle = importOpenFoodFactsDataArticle(articleDto);
        if(savedArticle == null){
            fileImportService.incrementNbLinesImported(importData);
            return;
        }

        boolean importedNutrients = importOpenFoodFactsDataNutrients(articleDto, savedArticle);

        int nbIngredients = importOpenFoodFactsDataIngredients(articleDto, savedArticle);

        fileImportService.incrementData(importData, importedNutrients, nbIngredients);

        fileImportService.incrementNbLinesImported(importData);
    }

    /**
     * Extracts the wanted article info out of an openfoodfacts dto article.
     * @param articleDto the dto containing info to store
     * @return an article entity that has been persisted, or null if a problem occured
     */
    private OpenFoodFactsArticleEntity importOpenFoodFactsDataArticle(OpenFoodFactsArticleDTO articleDto){

        OpenFoodFactsArticleEntity article = openFoodFactsArticleRepository.findByOpenFFId(articleDto.getId());
        article = openFoodFactsArticleEntityFactory.buildOrMergeArticle(articleDto, article);

        //Drop articles with empty names if asked to avoid dirty data
        if(Boolean.FALSE.equals(openFoodBusinessConfig.getImportEmptyArticles()) && StringUtils.isBlank(article.getProductName())){
            return null;
        }

        try {

            return openFoodFactsArticleRepository.save(article);
        } catch (DataAccessException e) {
            log.warn("importOpenFoodFactsDataArticle : error on article with id {}", articleDto.getId());
            return null;
        }
    }

    /**
     * Extracts the nutrient information out of an openfoodfacts dto.
     * @param articleDto the target dto where the info is stored
     * @param article the target article to link this nutrient info in out database
     * @return a boolean if an error occured
     */
    private boolean importOpenFoodFactsDataNutrients(OpenFoodFactsArticleDTO articleDto, OpenFoodFactsArticleEntity article) {

        if(articleDto.getNutrientLevels() == null || (Boolean.FALSE.equals(openFoodBusinessConfig.getImportEmptyNutrients()) && articleDto.getNutrientLevels().isEmpty())){
           return false;
        }

        OpenFoodFactsNutrientLevelsEntity nutrients = openFoodFactsNutrientLevelsRepository.findByArticleId(article.getId());
        nutrients = openFoodFactsNutrientLevelsEntityFactory.buildOrMergeNutrient(articleDto.getNutrientLevels(), nutrients);
        nutrients.setArticle(article);

        try {

            openFoodFactsNutrientLevelsRepository.save(nutrients);
        } catch (DataAccessException e) {
            log.warn("importOpenFoodFactsDataNutrients : error saving data", e);
            return false;
        }

        return true;
    }

    /**
     * Extracts the ingredient data from an openfoodfacts dto.
     * @param articleDto the target articledto where the info is stored
     * @param article the article to link ingredients with
     * @return the number of ingredients extracted from the dto (removing duplicates)
     */
    private int importOpenFoodFactsDataIngredients(OpenFoodFactsArticleDTO articleDto, OpenFoodFactsArticleEntity article){

        if(CollectionUtils.isEmpty(articleDto.getIngredients()) || article == null){
            return 0;
        }

        List<OpenFoodFactsIngredientEntity> ingredients = new ArrayList<>();

        articleDto.getIngredients().stream().forEach(i -> {
            OpenFoodFactsIngredientEntity ingredient = openFoodFactsIngredientsRepository.findByOpenFFIdAndArticle(i.getId(), article);
            ingredient = openFoodFactsIngredientEntityFactory.buildOrMergeIngredient(i, ingredient);
            ingredient.setArticle(article);
            ingredients.add(ingredient);
        });

        //Remove duplicated ids
        HashSet<Object> seen=new HashSet<>();
        ingredients.removeIf(e->!seen.add(e.getOpenFoodFactsId()));

        try {

            openFoodFactsIngredientsRepository.saveAll(ingredients);
        } catch (DataAccessException e) {
            log.warn("importOpenFoodFactsDataIngredients : error saving data", e);
            return 0;
        }

        return ingredients.size();
    }
}
