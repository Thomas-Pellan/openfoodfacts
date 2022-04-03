package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsIngredientEntity;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsIngredientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpenFoodFactsIngredientEntityFactory {

    public OpenFoodFactsIngredientEntity buildOrMergeIngredient(OpenFoodFactsIngredientDTO dto, OpenFoodFactsIngredientEntity old){

        if(dto == null){
            return  null;
        }

        OpenFoodFactsIngredientEntity entity = new OpenFoodFactsIngredientEntity();
        if(old != null){
            entity = old;
        }

        entity.setOpenFoodFactsId(dto.getId());
        entity.setFromPalmOil(dto.getFromPalmOil());
        entity.setOpenFoodFactsId(dto.getId());
        entity.setLabel(dto.getLabel());
        entity.setVegan(dto.getVegan());
        entity.setVegetarian(dto.getVegetarian());
        entity.setPercent(dto.getPercent());

        return entity;
    }
}
