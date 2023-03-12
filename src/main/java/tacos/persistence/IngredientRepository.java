package tacos.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import tacos.domain.Ingredient;

public interface IngredientRepository extends Repository<Ingredient, String> {

    List<Ingredient> findAll();

    Optional<Ingredient> findById(String id);
}
