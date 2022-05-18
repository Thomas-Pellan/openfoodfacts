package fr.pellan.api.openfoodfacts.events;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Event publisher for the openfoodfacts file import event.
 */
@Component
public class OpenFoodFactsFileImportEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes a file import event.
     * @param file the event target file
     */
    public void publishImportFileEvent(final OpenFoodFactsFileEntity file) {

        OpenFoodFactsFileImportEvent event = new OpenFoodFactsFileImportEvent(this, file);
        applicationEventPublisher.publishEvent(event);
    }
}
