package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileImportEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Factory building file import data entities.
 */
@Slf4j
@Service
public class OpenFoodFactsFileImportEntityFactory {

    public OpenFoodFactsFileImportEntity buildImportData(OpenFoodFactsFileEntity file){


        OpenFoodFactsFileImportEntity entity = new OpenFoodFactsFileImportEntity();
        entity.setFile(file);
        entity.setNbNutrients(0);
        entity.setNbLines(0);
        entity.setNbLinesImported(0);
        entity.setNbArticles(0);
        entity.setNbIngredients(0);

        return entity;
    }
}
