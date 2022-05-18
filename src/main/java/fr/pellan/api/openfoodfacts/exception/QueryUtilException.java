package fr.pellan.api.openfoodfacts.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception to handle an error while querying an url.
 */
@Slf4j
public class QueryUtilException extends RuntimeException {

    public QueryUtilException(String message) {
        super(message);
        if(log.isDebugEnabled()){
            log.debug(message);
        }
    }
}
