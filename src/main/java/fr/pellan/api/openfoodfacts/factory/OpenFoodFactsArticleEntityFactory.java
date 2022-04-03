package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsArticleEntity;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsArticleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Service
public class OpenFoodFactsArticleEntityFactory {

    public OpenFoodFactsArticleEntity buildOrMergeArticle(OpenFoodFactsArticleDTO dto, OpenFoodFactsArticleEntity old){

        if(dto == null){
            return  null;
        }

        OpenFoodFactsArticleEntity entity = new OpenFoodFactsArticleEntity();
        if(old != null){
            entity = old;
        }

        entity.setOpenFoodFactsId(dto.getId());

        Long unixCreated = Long.parseLong(dto.getCreated());
        entity.setCreationDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(unixCreated),
                TimeZone.getDefault().toZoneId()));

        entity.setEcoScore(dto.getEcoScore());
        entity.setIngredientsNb(dto.getIngredientsNb());
        entity.setProductName(dto.getProductNameShort());
        entity.setBrands(dto.getBrands());

        return entity;
    }
}
