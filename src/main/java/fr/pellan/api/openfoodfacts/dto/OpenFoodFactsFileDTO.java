package fr.pellan.api.openfoodfacts.dto;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * The openfoodfacts file dto.
 */
@Data
public class OpenFoodFactsFileDTO {

    private Long id;

    private String fileName;

    private LocalDateTime fileQueryTime;

    private OpenFoodFactsFileStatus lastImportStatus;
}
