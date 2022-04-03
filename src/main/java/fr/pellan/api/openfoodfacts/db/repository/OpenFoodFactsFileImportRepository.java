package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileImportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenFoodFactsFileImportRepository extends CrudRepository<OpenFoodFactsFileImportEntity, Integer> {

}
