package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileImportEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The file import repo.
 */
@Repository
public interface OpenFoodFactsFileImportRepository extends CrudRepository<OpenFoodFactsFileImportEntity, Long> {

    @Query(value = "SELECT f FROM OpenFoodFactsFileImportEntity f WHERE f.file.id = :fileId ORDER BY f.startDate ASC")
    List<OpenFoodFactsFileImportEntity> findByFileId(Long fileId);
}
