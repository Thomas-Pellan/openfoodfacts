package fr.pellan.api.openfoodfacts.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OpenFoodFactsArticleDTO implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("created_t")
    private String created;

    @SerializedName("abbreviated_product_name")
    private String productNameShort;

    @SerializedName("ingredients_n")
    private Integer ingredientsNb;

    @SerializedName("brands")
    private String brands;

    @SerializedName("ecoscore_score")
    private String ecoScore;

    @SerializedName("nutrient_levels")
    private OpenFoodFactsNutrientLevelsDTO nutrientLevels;

    @SerializedName("ingredients")
    private List<OpenFoodFactsIngredientDTO> ingredients;

}