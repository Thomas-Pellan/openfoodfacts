package fr.pellan.api.openfoodfacts.dto;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Dto used to store user criterias to search for file data.
 */
@Data
public class OpenFoodFactsImportInputDTO {

    private LocalDateTime start;

    private LocalDateTime end;

    private OpenFoodFactsFileStatus status;
}
