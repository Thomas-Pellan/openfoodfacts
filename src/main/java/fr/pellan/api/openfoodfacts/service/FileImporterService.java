package fr.pellan.api.openfoodfacts.service;

import fr.pellan.api.openfoodfacts.events.OpenFoodFactsFileImportEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileImporterService {

    @EventListener
    public void importFileEventListener(OpenFoodFactsFileImportEvent event){

        log.info("I'm doing something on the import of file {}", event.getFile().getFileName());
    }
}
