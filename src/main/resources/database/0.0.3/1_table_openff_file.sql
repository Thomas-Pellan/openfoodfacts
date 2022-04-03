CREATE TABLE openfoodfacts.file_import
(
    id INT NOT NULL AUTO_INCREMENT,
    id_file INT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME DEFAULT NULL,
    import_status VARCHAR(50) NOT NULL,
    nb_lines INT DEFAULT 0,
    nb_lines_imported INT DEFAULT 0,
    nb_articles INT DEFAULT 0,
    nb_ingredients INT DEFAULT 0,
    nb_nutrients INT DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE (id_file, start_date),
    FOREIGN KEY fk_file(id_file) references openfoodfacts.file(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;