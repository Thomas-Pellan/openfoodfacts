package fr.pellan.api.openfoodfacts.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple healthcheck controller.
 */
@Slf4j
@RestController
@RequestMapping("actuator")
public class HeathController {

    @GetMapping(value = "/health")
    public ResponseEntity<String> getFileImport(){

        return new ResponseEntity("UP",HttpStatus.OK);
    }
}
