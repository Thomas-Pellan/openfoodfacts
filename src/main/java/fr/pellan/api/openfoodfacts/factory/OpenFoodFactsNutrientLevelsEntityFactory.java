package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsNutrientLevelsEntity;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsNutrientLevelsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Factory building nutrient entities from openfoodfacts dtos.
 */
@Slf4j
@Service
public class OpenFoodFactsNutrientLevelsEntityFactory {

    public OpenFoodFactsNutrientLevelsEntity buildOrMergeNutrient(OpenFoodFactsNutrientLevelsDTO dto, OpenFoodFactsNutrientLevelsEntity old){

        if(dto == null){
            return  null;
        }

        OpenFoodFactsNutrientLevelsEntity entity = new OpenFoodFactsNutrientLevelsEntity();
        if(old != null){
            entity = old;
        }

        entity.setFat(dto.getFat());
        entity.setSalt(dto.getSalt());
        entity.setSugars(dto.getSugars());
        entity.setSaturatedFat(dto.getSaturatedFat());

        return entity;
    }
}
