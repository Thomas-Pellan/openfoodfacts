CREATE SCHEMA openfoodfacts;

CREATE TABLE openfoodfacts.configuration
(
    id INT NOT NULL AUTO_INCREMENT,
    property_key VARCHAR(50) NOT NULL,
    property_value VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);