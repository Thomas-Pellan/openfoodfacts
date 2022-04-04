package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileDTO;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsImportInputDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.service.FileDeltaImporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("open-food-facts/files")
public class OpenFoodFactsFileController {

    @Autowired
    private FileDeltaImporterService fileImporterService;

    @GetMapping(value = "/delta")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> getOpenFoodFactsDataDeltaFiles(){

        return new ResponseEntity(fileImporterService.saveOpenFoodFactsFileDelta(), HttpStatus.OK);
    }

    @PostMapping(value = "/find")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> findOpenFoodFactsFiles(@RequestBody OpenFoodFactsImportInputDTO input){

        return new ResponseEntity(fileImporterService.findOpenFoodFactsFiles(input), HttpStatus.OK);
    }

    @PostMapping(value = "/import")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> importOpenFoodFactsDataDeltaFiles(@RequestBody OpenFoodFactsImportInputDTO input){

        if(input == null || input.getStatus() == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        if(OpenFoodFactsFileStatus.IMPORT_STARTED == input.getStatus()){
            log.error("importOpenFoodFactsDataDeltaFiles : can't import something that's already stared (for now)");
            return new ResponseEntity(false, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity(fileImporterService.importAllFilesWithStatus(input), HttpStatus.OK);
    }
}
