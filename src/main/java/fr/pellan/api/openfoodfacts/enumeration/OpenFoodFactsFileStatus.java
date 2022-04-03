package fr.pellan.api.openfoodfacts.enumeration;

public enum OpenFoodFactsFileStatus {

    WAITING_FOR_IMPORT,
    IMPORT_STARTED,
    IMPORT_FINISHED,
    IMPORT_FAILED,
    IMPORT_FILE_UNREACHABLE,
    IMPORT_FILE_EMPTY,
    EMPTY;
}
