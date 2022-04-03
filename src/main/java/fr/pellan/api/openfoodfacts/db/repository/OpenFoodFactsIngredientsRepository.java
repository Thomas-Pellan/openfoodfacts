package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsArticleEntity;
import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsIngredientEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenFoodFactsIngredientsRepository extends CrudRepository<OpenFoodFactsIngredientEntity, Integer> {

    @Query(value = "SELECT i FROM OpenFoodFactsIngredientEntity i WHERE i.openFoodFactsId = :openffId AND i.article = :article")
    OpenFoodFactsIngredientEntity findByOpenFFIdAndArticle(String openffId, OpenFoodFactsArticleEntity article);
}
