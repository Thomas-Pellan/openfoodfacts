package fr.pellan.api.openfoodfacts.service;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileImportEntity;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileImportRepository;
import fr.pellan.api.openfoodfacts.db.repository.OpenFoodFactsFileRepository;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileImportDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.factory.OpenFoodFactsFileImportDTOFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class handling a file import with it's status during the import.
 */
@Slf4j
@Service
public class FileImportService {

    @Autowired
    private OpenFoodFactsFileRepository openFoodFactsFileRepository;

    @Autowired
    private OpenFoodFactsFileImportRepository openFoodFactsFileImportRepository;

    @Autowired
    private OpenFoodFactsFileImportDTOFactory openFoodFactsFileImportDTOFactory;

    /**
     * Triggers the start of a file import.
     * @param file the target file data
     * @param fileImport the imported process data
     * @return an updated process data (started)
     */
    public OpenFoodFactsFileImportEntity startImport(OpenFoodFactsFileEntity file, OpenFoodFactsFileImportEntity fileImport) {

        OpenFoodFactsFileStatus status = OpenFoodFactsFileStatus.IMPORT_STARTED;
        file.setFileStatus(status);
        fileImport.setFileStatus(status);
        fileImport.setStartDate(LocalDateTime.now());

        try {
            openFoodFactsFileRepository.save(file);

            return openFoodFactsFileImportRepository.save(fileImport);

        } catch (DataAccessException e) {
            log.error("startImport : Error while starting import", e);
        }
        return null;
    }

    /**
     * Ends the import on a file.
     * @param file the target file data
     * @param fileImport the imported process data
     * @param status an updated process data (ended)
     */
    public void endImport(OpenFoodFactsFileEntity file, OpenFoodFactsFileImportEntity fileImport, OpenFoodFactsFileStatus status) {

        file.setFileStatus(status);
        fileImport.setFileStatus(status);
        fileImport.setEndDate(LocalDateTime.now());

        try {
            openFoodFactsFileRepository.save(file);

            openFoodFactsFileImportRepository.save(fileImport);

        } catch (DataAccessException e) {
            log.error("startImport : Error while starting import", e);
        }
    }

    /**
     * Increments the number of lines imported in database from a file.
     * @param importData the import process data
     */
    public void incrementNbLinesImported(OpenFoodFactsFileImportEntity importData){

        importData.setNbLinesImported(importData.getNbLinesImported() + 1);

        openFoodFactsFileImportRepository.save(importData);
    }

    /**
     * Increments a file import process number of sub infos from lines of the files, as nutrients and ingredients
     * @param importData the import process data
     * @param importedNutrients boolean to increment nutrients of found
     * @param nbIngredients the number of ingredients found to increment the data with
     */
    public void incrementData(OpenFoodFactsFileImportEntity importData, boolean importedNutrients, int nbIngredients){

        importData.setNbArticles(importData.getNbArticles() + 1);
        if(importedNutrients){
            importData.setNbNutrients(importData.getNbNutrients() + 1);
        }
        importData.setNbIngredients(importData.getNbIngredients() + nbIngredients);

        openFoodFactsFileImportRepository.save(importData);
    }

    /***
     * Finds a file import process data by id
     * @param id the target id
     * @return a file import process entity, if found
     */
    public OpenFoodFactsFileImportDTO findById(Long id) {

        return openFoodFactsFileImportDTOFactory.buildFileImportDto(openFoodFactsFileImportRepository.findById(id).orElse(null));
    }

    /***
     * Finds the last file import process data for a target file id
     * @param fileId the target file id
     * @return a file import process entity, if found
     */
    public OpenFoodFactsFileImportDTO findLastByFileId(Long fileId) {

        return openFoodFactsFileImportDTOFactory.buildFileImportDto(openFoodFactsFileImportRepository.findByFileId(fileId).get(0));
    }

    /***
     * Finds all file import process data for a target file id
     * @param fileId the target file id
     * @return all the import data for this file
     */
    public List<OpenFoodFactsFileImportDTO> findByFileId(Long fileId) {

        return openFoodFactsFileImportDTOFactory.buildFileImportDtos(openFoodFactsFileImportRepository.findByFileId(fileId));
    }
}
