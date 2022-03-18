package fr.pellan.api.openfoodfacts.controller;

import fr.pellan.api.openfoodfacts.service.FileImporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("open-food-facts")
public class OpenFoodFactsImporterController {

    @Autowired
    private FileImporterService fileImporterService;

    @GetMapping(value = "/delta-files")
    public void getOpenFoodFactsDataDeltaFiles(){

        fileImporterService.saveOpenFoodFactsFileDelta();
    }

    @GetMapping(value = "/import")
    public void importOpenFoodFactsDataDeltaFiles(){

    }
}
