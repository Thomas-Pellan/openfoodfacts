package fr.pellan.api.openfoodfacts.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OpenFoodBusinessConfig {

    @Value("${openfood.import.empty}")
    private Boolean importEmpty;
}
