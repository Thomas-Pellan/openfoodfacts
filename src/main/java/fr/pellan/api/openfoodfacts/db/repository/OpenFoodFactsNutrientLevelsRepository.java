package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsNutrientLevelsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The nutrient repo.
 */
@Repository
public interface OpenFoodFactsNutrientLevelsRepository extends CrudRepository<OpenFoodFactsNutrientLevelsEntity, Integer> {

    @Query(value = "SELECT n FROM OpenFoodFactsNutrientLevelsEntity n WHERE n.article.id = :articleId")
    OpenFoodFactsNutrientLevelsEntity findByArticleId(Long articleId);
}
