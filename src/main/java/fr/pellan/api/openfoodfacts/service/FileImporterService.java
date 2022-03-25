package fr.pellan.api.openfoodfacts.service;

import fr.pellan.api.openfoodfacts.config.OpenFoodApiConfig;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileRepository;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.events.OpenFoodFactsFileImportEvent;
import fr.pellan.api.openfoodfacts.exception.OpenFoodFactsFileImportException;
import fr.pellan.api.openfoodfacts.util.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

        } catch (IOException e) {
            log.error(String.format("importFile : error while getting %s content, data not imported", file.getFileName()), e);
            throw new OpenFoodFactsFileImportException("error during file parse");
        }
    }
}
