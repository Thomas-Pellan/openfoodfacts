package fr.pellan.api.openfoodfacts.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class OpenFoodFactsIngredientDTO implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("vegetarian")
    private String vegetarian;

    @SerializedName("vegan")
    private String vegan;

    @SerializedName("from_palm_oil")
    private String fromPalmOil;

    @SerializedName("text")
    private String label;

    @SerializedName("percent")
    private Double percent;
}
