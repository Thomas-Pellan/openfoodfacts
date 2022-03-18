package fr.pellan.api.openfoodfacts.service;

import fr.pellan.api.openfoodfacts.config.OpenFoodApiConfig;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileRepository;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.util.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FileImporterService {

    @Autowired
    private OpenFoodApiConfig openFoodApiConfig;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private OpenFoodFactsFileRepository openFoodFactsFileRepository;

    private static final String OPENFFAPI_FILE_SEPARATOR = "\n";

    private static final String INDEX_URL = "index.txt";

    private String[] getOpenApiFileList() {

        String files = queryUtil.getDataAsString(openFoodApiConfig.getStaticDataFilesUrl() + INDEX_URL);
        if (StringUtils.isBlank(files)) {
            return null;
        }

        return files.split(OPENFFAPI_FILE_SEPARATOR);
    }

    public boolean saveOpenFoodFactsFileDelta() {

        String[] fileList = getOpenApiFileList();
        if(fileList == null) {
            return false;
        }

        List<OpenFoodFactsFileEntity> files = new ArrayList<>();
        Arrays.stream(fileList).forEach(f -> {

            OpenFoodFactsFileEntity file = openFoodFactsFileRepository.findByFileName(f);
            if(file != null){
                return;
            }

            file = new OpenFoodFactsFileEntity();
            file.setFileName(f);
            file.setFileQueryTime(LocalDateTime.now());
            file.setFileStatus(OpenFoodFactsFileStatus.WAITING_FOR_IMPORT);
            files.add(file);
        });

        if(!CollectionUtils.isEmpty(files)){
            try{
                openFoodFactsFileRepository.saveAll(files);
            } catch(DataAccessException e){
                log.error("saveOpenFoodFactsFileDelta : persistence error", e);
                return false;
            }
        }

        return true;
    }
}
