package fr.pellan.api.openfoodfacts.enumeration;

/**
 * The file status enumeration.
 * // TODO: 18/05/2022 get this in the database in an entity with a converted to keep this enum java side
 */
public enum OpenFoodFactsFileStatus {

    WAITING_FOR_IMPORT,
    IMPORT_STARTED,
    IMPORT_FINISHED,
    IMPORT_FAILED,
    IMPORT_FILE_UNREACHABLE,
    IMPORT_FILE_EMPTY,
    EMPTY;
}
