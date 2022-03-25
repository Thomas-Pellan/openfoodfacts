package fr.pellan.api.openfoodfacts.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OpenFoodApiConfig {

    @Value("${openfood.api.data.url}")
    private String staticDataFilesUrl;

    @Value("${openfood.api.data.separator}")
    private String fileSeparator;
}
