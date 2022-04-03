package fr.pellan.api.openfoodfacts.db.repository;

import fr.pellan.api.openfoodfacts.db.entity.OpenFoodFactsFileEntity;
import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OpenFoodFactsFileRepository extends CrudRepository<OpenFoodFactsFileEntity, Integer> {

    @Query(value = "SELECT f FROM OpenFoodFactsFileEntity f WHERE f.fileName = :fileName")
    OpenFoodFactsFileEntity findByFileName(String fileName);

    @Query(value = "SELECT f FROM OpenFoodFactsFileEntity f WHERE f.fileStatus = :status")
    List<OpenFoodFactsFileEntity> findByStatus(OpenFoodFactsFileStatus status);

    @Query(value = "SELECT f " +
            "FROM OpenFoodFactsFileEntity f " +
            "WHERE f.fileStatus = :status " +
            "AND f.fileQueryTime BETWEEN :start AND :end ")
    List<OpenFoodFactsFileEntity> findByStatusAndDates(OpenFoodFactsFileStatus status, LocalDateTime start, LocalDateTime end);
}
