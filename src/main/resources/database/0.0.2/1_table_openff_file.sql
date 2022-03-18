CREATE TABLE openfoodfacts.file
(
    id INT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(100) NOT NULL,
    file_query_date DATETIME NOT NULL,
    file_status VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (file_name)
);