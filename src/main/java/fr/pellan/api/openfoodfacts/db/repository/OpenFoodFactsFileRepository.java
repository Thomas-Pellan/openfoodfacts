package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenFoodFactsFileRepository extends CrudRepository<OpenFoodFactsFileEntity, Integer> {

    @Query(value = "SELECT f FROM OpenFoodFactsFileEntity f WHERE f.fileName = :fileName")
    OpenFoodFactsFileEntity findByFileName(String fileName);
}
