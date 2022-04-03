package fr.pellan.api.openfoodfacts.db.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "nutrient_levels", schema = "openfoodfacts")
public class OpenFoodFactsNutrientLevelsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_article")
    private OpenFoodFactsArticleEntity article;

    @Column(name = "saturated_fat")
    private String saturatedFat;

    @Column(name = "fat")
    private String fat;

    @Column(name = "salt")
    private String salt;

    @Column(name = "sugars")
    private String sugars;
}
