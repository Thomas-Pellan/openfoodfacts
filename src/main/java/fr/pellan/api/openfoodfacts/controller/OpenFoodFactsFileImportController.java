package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileImportDTO;
import fr.pellan.api.openfoodfacts.service.FileDeltaImporterService;
import fr.pellan.api.openfoodfacts.service.FileImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("open-food-facts/file-import")
public class OpenFoodFactsFileImportController {

    @Autowired
    private FileImportService fileImportService;

    @GetMapping(value = "/status")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileImport(@RequestParam(value = "fileImportId") Long  fileImportId){

        return new ResponseEntity(fileImportService.findById(fileImportId), HttpStatus.OK);
    }

    @GetMapping(value = "/last-status")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileCurrentStatus(@RequestParam(value = "fileId") Long  fileId){

        return new ResponseEntity(fileImportService.findLastByFileId(fileId), HttpStatus.OK);
    }

    @GetMapping(value = "/status-history")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileImportStatusHistory(@RequestParam(value = "fileId") Long  fileId){

        return new ResponseEntity(fileImportService.findByFileId(fileId), HttpStatus.OK);
    }
}
