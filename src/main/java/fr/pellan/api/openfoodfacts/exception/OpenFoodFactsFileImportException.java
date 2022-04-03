package fr.pellan.api.openfoodfacts.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenFoodFactsFileImportException extends RuntimeException {

    public OpenFoodFactsFileImportException(String message) {
        super(message);
        if(log.isDebugEnabled()){
            log.debug(message);
        }
    }
}
