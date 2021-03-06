package fr.pellan.api.openfoodfacts.factory;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsArticleEntity;
import fr.pellan.api.openfoodfacts.dto.OpenFoodFactsArticleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Factory building article entities from openfoodfacts article dto.
 */
@Slf4j
@Service
public class OpenFoodFactsArticleEntityFactory {

    /**
     * Builds a new entity or merges and exitsing one with the given dto data.
     * @param dto the new dto containing data
     * @param old the existing article in the database
     * @return an new article or updated article object
     */
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
        if(unixCreated != null){
            entity.setCreationDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(unixCreated),
                    TimeZone.getDefault().toZoneId()));
        }

        entity.setEcoScore(dto.getEcoScore());
        entity.setIngredientsNb(dto.getIngredientsNb());
        entity.setProductName(dto.getProductNameShort());
        entity.setBrands(dto.getBrands());

        return entity;
    }
}
