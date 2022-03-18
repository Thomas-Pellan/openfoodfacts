package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.service.FileDeltaImporterService;
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
@RequestMapping("open-food-facts")
public class OpenFoodFactsImporterController {

    @Autowired
    private FileDeltaImporterService fileImporterService;

    @GetMapping(value = "/delta-files")
    public ResponseEntity<Boolean> getOpenFoodFactsDataDeltaFiles(){

        return new ResponseEntity(fileImporterService.saveOpenFoodFactsFileDelta(), HttpStatus.OK);
    }

    @GetMapping(value = "/import")
    public ResponseEntity<Boolean> importOpenFoodFactsDataDeltaFiles(@RequestParam(value= "status") OpenFoodFactsFileStatus status){

        if(OpenFoodFactsFileStatus.IMPORT_FINISHED == status){
            log.warn("importOpenFoodFactsDataDeltaFiles : trying to reimport already imported files, this may cause problems");
        }

        if(OpenFoodFactsFileStatus.IMPORT_STARTED == status){
            log.error("importOpenFoodFactsDataDeltaFiles : can't import something that's already stared (for now)");
            return new ResponseEntity(false, HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity(fileImporterService.importAllFilesWithStatus(status), HttpStatus.OK);
    }
}
