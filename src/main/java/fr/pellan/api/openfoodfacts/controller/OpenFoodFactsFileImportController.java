package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileImportDTO;
import fr.pellan.api.openfoodfacts.service.FileImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller exposing data on the openfoodfacts import process.
 */
@Slf4j
@RestController
@RequestMapping("open-food-facts/file-import")
public class OpenFoodFactsFileImportController {

    @Autowired
    private FileImportService fileImportService;

    /**
     * Gets file import data
     * @param fileImportId the target file import id
     * @return a file import dto
     */
    @GetMapping(value = "/status")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileImport(@RequestParam(value = "fileImportId") Long  fileImportId){

        return new ResponseEntity(fileImportService.findById(fileImportId), HttpStatus.OK);
    }

    /**
     * Gets last status on the target file import
     * @param fileId the target file id
     * @return a dto containing info on the import if found
     */
    @GetMapping(value = "/last-status")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileCurrentStatus(@RequestParam(value = "fileId") Long  fileId){

        return new ResponseEntity(fileImportService.findLastByFileId(fileId), HttpStatus.OK);
    }

    /**
     * Gets all import data on the target file
     * @param fileId the target file id
     * @return a file import data stored by dates for this file
     */
    @GetMapping(value = "/status-history")
    public ResponseEntity<List<OpenFoodFactsFileImportDTO>> getFileImportStatusHistory(@RequestParam(value = "fileId") Long  fileId){

        return new ResponseEntity(fileImportService.findByFileId(fileId), HttpStatus.OK);
    }
}
