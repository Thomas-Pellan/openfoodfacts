package fr.pellan.api.openfoodfacts.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OpenFoodFactsIngredientDTO implements Serializable {

    @SerializedName("vegetarian")
    private String vegetarian;

    @SerializedName("vegan")
    private String vegan;

    @SerializedName("id")
    private String id;

    @SerializedName("from_palm_oil")
    private String fromPalmOil;

    @SerializedName("has_sub_ingredients")
    private String hasSubIngredients;

    @SerializedName("ingredients")
    private List<OpenFoodFactsIngredientDTO> subIngredients;

    @SerializedName("text")
    private String label;

    @SerializedName("rank")
    private Integer rank;
}
