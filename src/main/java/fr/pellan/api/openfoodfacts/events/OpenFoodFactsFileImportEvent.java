package fr.pellan.api.openfoodfacts.events;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OpenFoodFactsFileImportEvent extends ApplicationEvent {

    private OpenFoodFactsFileEntity file;

    public OpenFoodFactsFileImportEvent(Object source, OpenFoodFactsFileEntity file) {
        super(source);
        this.file = file;
    }
}
