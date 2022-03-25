package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsImportInputDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.service.FileDeltaImporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("open-food-facts")
public class OpenFoodFactsImporterController {

    @Autowired
    private FileDeltaImporterService fileImporterService;

    @GetMapping(value = "/delta-files")
    public ResponseEntity<Boolean> getOpenFoodFactsDataDeltaFiles(){

        return new ResponseEntity(fileImporterService.saveOpenFoodFactsFileDelta(), HttpStatus.OK);
    }

    @PostMapping(value = "/import")
    public ResponseEntity<Boolean> importOpenFoodFactsDataDeltaFiles(@RequestBody OpenFoodFactsImportInputDTO input){

        if(input == null || input.getStatus() == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        if(OpenFoodFactsFileStatus.IMPORT_FINISHED == input.getStatus()){
            log.warn("importOpenFoodFactsDataDeltaFiles : trying to reimport already imported files, this may cause problems");
        }

        if(OpenFoodFactsFileStatus.IMPORT_STARTED == input.getStatus()){
            log.error("importOpenFoodFactsDataDeltaFiles : can't import something that's already stared (for now)");
            return new ResponseEntity(false, HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity(fileImporterService.importAllFilesWithStatus(input), HttpStatus.OK);
    }
}
