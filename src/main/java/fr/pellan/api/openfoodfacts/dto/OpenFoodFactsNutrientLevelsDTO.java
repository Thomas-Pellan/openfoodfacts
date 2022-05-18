package fr.pellan.api.openfoodfacts.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * The openfoodfacts api nutrient dto.
 */
@Data
public class OpenFoodFactsNutrientLevelsDTO implements Serializable {


    @SerializedName("saturated-fat")
    private String saturatedFat;

    @SerializedName("fat")
    private String fat;

    @SerializedName("salt")
    private String salt;

    @SerializedName("sugars")
    private String sugars;

    public boolean isEmpty() {
        return StringUtils.isBlank(saturatedFat)
                && StringUtils.isBlank(fat)
                && StringUtils.isBlank(salt)
                && StringUtils.isBlank(sugars);
    }
}
