package tacos.persistence;

import java.util.List;
import java.util.Optional;

import tacos.domain.Ingredient;

public interface IngredientRepository {

    List<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);
}
