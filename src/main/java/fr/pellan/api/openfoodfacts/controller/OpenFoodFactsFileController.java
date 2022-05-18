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

/**
 * Controller exposing data of openfoodfacts files and triggering file import.
 */
@Slf4j
@RestController
@RequestMapping("open-food-facts/files")
public class OpenFoodFactsFileController {

    @Autowired
    private FileDeltaImporterService fileImporterService;

    /**
     * Gets the delta files exposed by the openfoodfacts API.
     * @return a list of file info
     */
    @GetMapping(value = "/delta")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> getOpenFoodFactsDataDeltaFiles(){

        return new ResponseEntity(fileImporterService.saveOpenFoodFactsFileDelta(), HttpStatus.OK);
    }

    /**
     * Gets the openfoodfacts file data in the database.
     * @param input the sreach criterias
     * @return a list of openfoodfacts file data
     */
    @PostMapping(value = "/find")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> findOpenFoodFactsFiles(@RequestBody OpenFoodFactsImportInputDTO input){

        return new ResponseEntity(fileImporterService.findOpenFoodFactsFiles(input), HttpStatus.OK);
    }

    /**
     * Tiggers import for all the files using given criterias.
     * @param input the file criterias
     * @return a list of files that will be imported
     */
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
