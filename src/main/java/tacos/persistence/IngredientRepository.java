package tacos.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

    List<Ingredient> findAll();
}
