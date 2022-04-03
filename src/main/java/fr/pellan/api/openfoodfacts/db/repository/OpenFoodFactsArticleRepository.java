package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsArticleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenFoodFactsArticleRepository extends CrudRepository<OpenFoodFactsArticleEntity, Integer> {

    @Query(value = "SELECT a FROM OpenFoodFactsArticleEntity a WHERE a.openFoodFactsId = :openffId")
    OpenFoodFactsArticleEntity findByOpenFFId(String openffId);
}
