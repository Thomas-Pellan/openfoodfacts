CREATE TABLE openfoodfacts.article
(
    id INT NOT NULL AUTO_INCREMENT,
    remote_id VARCHAR(100) NOT NULL,
    creation_date DATETIME NOT NULL,
    product_name VARCHAR(100) DEFAULT NULL,
    nb_ingredients INT DEFAULT 0,
    brands VARCHAR(100) DEFAULT NULL,
    ecoscore INT DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (remote_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE openfoodfacts.nutrient_levels
(
    id INT NOT NULL AUTO_INCREMENT,
    id_article INT NOT NULL,
    saturated_fat VARCHAR(10) DEFAULT NULL,
    fat VARCHAR(10) DEFAULT NULL,
    salt VARCHAR(10) DEFAULT NULL,
    sugars VARCHAR(10) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE (id_article),
    FOREIGN KEY fk_nutrient_article(id_article) references openfoodfacts.article(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE openfoodfacts.ingredient
(
    id INT NOT NULL AUTO_INCREMENT,
    id_article INT NOT NULL,
    remote_id VARCHAR(200) DEFAULT NULL,
    vegetarian VARCHAR(10) DEFAULT NULL,
    vegan VARCHAR(10) DEFAULT NULL,
    from_palm_oil VARCHAR(10) DEFAULT NULL,
    label VARCHAR(200) DEFAULT NULL,
    percent DOUBLE DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_ingredient UNIQUE (id_article, remote_id),
    FOREIGN KEY fk_ingredient_article(id_article) references openfoodfacts.article(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
