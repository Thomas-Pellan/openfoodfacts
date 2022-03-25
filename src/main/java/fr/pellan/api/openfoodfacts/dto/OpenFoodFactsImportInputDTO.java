package fr.pellan.api.openfoodfacts.dto;

import fr.pellan.api.openfoodfacts.enumeration.OpenFoodFactsFileStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OpenFoodFactsImportInputDTO {

    private LocalDateTime start;

    private LocalDateTime end;

    private OpenFoodFactsFileStatus status;
}
