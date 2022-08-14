package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileDTO;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsImportInputDTO;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import fr.pellan.api.openfoodfacts.service.FileDeltaImporterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing data of openfoodfacts files and triggering file import.
 */
@Tag(name = "File info Controller")
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
    @Operation(summary = "Get openfoodfacts file delta",
             description = "Sends a query to the openfoodfact API to get the current list of files available")
    @GetMapping(value = "/delta")
    @ApiResponse(responseCode = "200", description = "Success")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> getOpenFoodFactsDataDeltaFiles(){

        return new ResponseEntity<>(fileImporterService.saveOpenFoodFactsFileDelta(), HttpStatus.OK);
    }

    /**
     * Gets the openfoodfacts file data in the database.
     * @param input the sreach criterias
     * @return a list of openfoodfacts file data
     */
    @Operation(summary = "File files info",
            description = "Gets the current file data stored in the database")
    @PostMapping(value = "/find")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "An error occuried")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> findOpenFoodFactsFiles(@RequestBody OpenFoodFactsImportInputDTO input){

        return new ResponseEntity<>(fileImporterService.findOpenFoodFactsFiles(input), HttpStatus.OK);
    }

    /**
     * Tiggers import for all the files using given criterias.
     * @param input the file criterias
     * @return a list of files that will be imported
     */
    @Operation(summary = "Trigger import on the selected files",
            description = "Will trigger an import on the criteria compatible files is they are in an accepted state")
    @PostMapping(value = "/import")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "An error occuried")
    public ResponseEntity<List<OpenFoodFactsFileDTO>> importOpenFoodFactsDataDeltaFiles(@RequestBody OpenFoodFactsImportInputDTO input){

        if(input == null || input.getStatus() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(OpenFoodFactsFileStatus.IMPORT_STARTED == input.getStatus()){
            log.error("importOpenFoodFactsDataDeltaFiles : can't import something that's already stared (for now)");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(fileImporterService.importAllFilesWithStatus(input), HttpStatus.OK);
    }
}
