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

@Slf4j
@Service
public class FileImportService {

    @Autowired
    private OpenFoodFactsFileRepository openFoodFactsFileRepository;

    @Autowired
    private OpenFoodFactsFileImportRepository openFoodFactsFileImportRepository;

    @Autowired
    private OpenFoodFactsFileImportDTOFactory openFoodFactsFileImportDTOFactory;

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

    public void incrementNbLinesImported(OpenFoodFactsFileImportEntity importData){

        importData.setNbLinesImported(importData.getNbLinesImported() + 1);

        openFoodFactsFileImportRepository.save(importData);
    }

    public void incrementData(OpenFoodFactsFileImportEntity importData, boolean importedNutrients, int nbIngredients){

        importData.setNbArticles(importData.getNbArticles() + 1);
        if(importedNutrients){
            importData.setNbNutrients(importData.getNbNutrients() + 1);
        }
        importData.setNbIngredients(importData.getNbIngredients() + nbIngredients);

        openFoodFactsFileImportRepository.save(importData);
    }

    public OpenFoodFactsFileImportDTO findById(Long id) {

        return openFoodFactsFileImportDTOFactory.buildFileImportDto(openFoodFactsFileImportRepository.findById(id).orElse(null));
    }

    public OpenFoodFactsFileImportDTO findLastByFileId(Long fileId) {

        return openFoodFactsFileImportDTOFactory.buildFileImportDto(openFoodFactsFileImportRepository.findByFileId(fileId).get(0));
    }

    public List<OpenFoodFactsFileImportDTO> findByFileId(Long fileId) {

        return openFoodFactsFileImportDTOFactory.buildFileImportDtos(openFoodFactsFileImportRepository.findByFileId(fileId));
    }
}
