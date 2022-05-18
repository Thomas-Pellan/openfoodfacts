package fr.pellan.api.openfoodfacts.dto;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * The openfoodfacts import process data dto.
 */
@Data
public class OpenFoodFactsFileImportDTO {

    private Long id;

    private OpenFoodFactsFileStatus fileStatus;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer nbLines;

    private Integer nbLinesImported;

    private Integer nbArticles;

    private Integer nbIngredients;

    private Integer nbNutrients;
}
