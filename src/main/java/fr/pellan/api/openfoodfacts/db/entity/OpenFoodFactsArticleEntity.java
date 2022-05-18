package fr.pellan.api.openfoodfacts.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The Article entity.
 */
@Data
@Entity
@Table(name = "article", schema = "openfoodfacts")
public class OpenFoodFactsArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "remote_id")
    private String openFoodFactsId;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "nb_ingredients")
    private Integer ingredientsNb;

    @Column(name = "brands")
    private String brands;

    @Column(name = "ecoscore")
    private Integer ecoScore;
}
