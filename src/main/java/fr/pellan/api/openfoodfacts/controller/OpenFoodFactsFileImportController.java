package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsFileImportDTO;
import fr.pellan.api.openfoodfacts.service.FileImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "File import data Controller")
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
    @Operation(summary = "Gets a file import data from it's id",
            description = "finds a file import data in the database using it's id")
    @GetMapping(value = "/status")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "An error occuried")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileImport(@RequestParam(value = "fileImportId") Long  fileImportId){

        return new ResponseEntity(fileImportService.findById(fileImportId), HttpStatus.OK);
    }

    /**
     * Gets last status on the target file import
     * @param fileId the target file id
     * @return a dto containing info on the import if found
     */
    @Operation(summary = "Gets a file current import data from the file id",
            description = "finds a file import data in the database using the file id")
    @GetMapping(value = "/last-status")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "An error occuried")
    public ResponseEntity<OpenFoodFactsFileImportDTO> getFileCurrentStatus(@RequestParam(value = "fileId") Long  fileId){

        return new ResponseEntity(fileImportService.findLastByFileId(fileId), HttpStatus.OK);
    }

    /**
     * Gets all import data on the target file
     * @param fileId the target file id
     * @return a file import data stored by dates for this file
     */
    @Operation(summary = "Gets a file import data history",
            description = "finds all import data available for the targeted file")
    @GetMapping(value = "/status-history")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "500", description = "An error occuried")
    public ResponseEntity<List<OpenFoodFactsFileImportDTO>> getFileImportStatusHistory(@RequestParam(value = "fileId") Long  fileId){

        return new ResponseEntity(fileImportService.findByFileId(fileId), HttpStatus.OK);
    }
}
