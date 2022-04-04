package fr.pellan.api.openfoodfacts.service;

import fr.pellan.api.openfoodfacts.config.OpenFoodApiConfig;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileRepository;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileDTO;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsImportInputDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.events.OpenFoodFactsFileImportEventPublisher;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsFileDTOFactory;
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
public class FileDeltaImporterService {

    @Autowired
    private OpenFoodApiConfig openFoodApiConfig;

    @Autowired
    private QueryUtil queryUtil;

    @Autowired
    private OpenFoodFactsFileRepository openFoodFactsFileRepository;

    @Autowired
    private OpenFoodFactsFileDTOFactory openFoodFactsFileDTOFactory;

    @Autowired
    private OpenFoodFactsFileImportEventPublisher openFoodFactsFileImportEventPublisher;

    private static final String INDEX_URL = "index.txt";

    private String[] getOpenApiFileList() {

        String files = queryUtil.getDataAsString(openFoodApiConfig.getStaticDataFilesUrl() + INDEX_URL);
        if (StringUtils.isBlank(files)) {
            return null;
        }

        return files.split(openFoodApiConfig.getFileSeparator());
    }

    public List<OpenFoodFactsFileDTO> saveOpenFoodFactsFileDelta() {

        String[] fileList = getOpenApiFileList();
        if(fileList == null) {
            return new ArrayList<>();
        }

        final List<OpenFoodFactsFileEntity> files = new ArrayList<>();
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

        List<OpenFoodFactsFileEntity> persistedFiles = new ArrayList<>();
        if(!CollectionUtils.isEmpty(files)){
            try{
                persistedFiles = (List<OpenFoodFactsFileEntity>) openFoodFactsFileRepository.saveAll(files);
            } catch(DataAccessException e){
                log.error("saveOpenFoodFactsFileDelta : persistence error", e);
                return new ArrayList<>();
            }
        }

        return openFoodFactsFileDTOFactory.buildFileDtos(persistedFiles);
    }

    public List<OpenFoodFactsFileDTO> findOpenFoodFactsFiles(OpenFoodFactsImportInputDTO input){

        return openFoodFactsFileDTOFactory.buildFileDtos(findFiles(input));
    }

    private List<OpenFoodFactsFileEntity> findFiles(OpenFoodFactsImportInputDTO input){

        List<OpenFoodFactsFileEntity> files;

        if(input.getStart() == null || input.getEnd() == null){
            files = openFoodFactsFileRepository.findByStatus(input.getStatus());
        } else {
            files = openFoodFactsFileRepository.findByStatusAndDates(input.getStatus(), input.getStart(), input.getEnd());
        }

        return files;
    }

    public List<OpenFoodFactsFileDTO> importAllFilesWithStatus(OpenFoodFactsImportInputDTO input){

        List<OpenFoodFactsFileEntity> files = findFiles(input);

        files.stream().forEach(f -> openFoodFactsFileImportEventPublisher.publishImportFileEvent(f));

        return openFoodFactsFileDTOFactory.buildFileDtos(files);
    }
}
