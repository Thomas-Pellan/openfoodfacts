package fr.pellan.api.openfoodfacts.db.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * The Ingredient entity.
 */
@Data
@Entity
@Table(name = "ingredient", schema = "openfoodfacts")
public class OpenFoodFactsIngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_article")
    private OpenFoodFactsArticleEntity article;

    @Column(name = "remote_id")
    private String openFoodFactsId;

    @Column(name = "vegetarian")
    private String vegetarian;

    @Column(name = "vegan")
    private String vegan;

    @Column(name = "from_palm_oil")
    private String fromPalmOil;

    @Column(name = "label")
    private String label;

    @Column(name = "percent")
    private Double percent;
}
