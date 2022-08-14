package fr.pellan.api.openfoodfacts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple healthcheck controller.
 */
@Tag(name = "Health checck for the app")
@Slf4j
@RestController
@RequestMapping("actuator")
public class HeathController {

    @Operation(summary = "Simple Health Check",
            description = "Sends back 'UP'")
    @GetMapping(value = "/health")
    @ApiResponse(responseCode = "200", description = "The app is up, hurray !")
    public ResponseEntity<String> getFileImport(){

        return new ResponseEntity<>("UP",HttpStatus.OK);
    }
}
