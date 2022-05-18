package fr.pellan.api.openfoodfacts.events;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Description of the openfoodfact file import event.
 */
@Getter
public class OpenFoodFactsFileImportEvent extends ApplicationEvent {

    /**
     * The file info used for import.
     */
    private OpenFoodFactsFileEntity file;

    /***
     * Constructor for the event.
     * @param source the event source
     * @param file the event data
     */
    public OpenFoodFactsFileImportEvent(Object source, OpenFoodFactsFileEntity file) {
        super(source);
        this.file = file;
    }
}
