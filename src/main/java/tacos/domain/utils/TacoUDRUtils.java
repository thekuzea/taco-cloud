package tacos.domain.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tacos.domain.Ingredient;
import tacos.domain.Taco;
import tacos.domain.udt.IngredientUDT;
import tacos.domain.udt.TacoUDT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TacoUDRUtils {

    public static IngredientUDT toIngredientUDT(final Ingredient ingredient) {
        return new IngredientUDT(ingredient.getId(), ingredient.getName(), ingredient.getType());
    }

    public static TacoUDT toTacoUDT(final Taco taco) {
        return new TacoUDT(taco.getName(), taco.getIngredients());
    }
}
