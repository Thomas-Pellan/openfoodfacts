package fr.pellan.api.openfoodfacts.events;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OpenFoodFactsFileImportEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishImportFileEvent(final OpenFoodFactsFileEntity file) {

        OpenFoodFactsFileImportEvent event = new OpenFoodFactsFileImportEvent(this, file);
        applicationEventPublisher.publishEvent(event);
    }
}
