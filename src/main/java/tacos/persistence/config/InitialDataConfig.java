package tacos.persistence.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.Taco;
import tacos.persistence.IngredientRepository;
import tacos.persistence.TacoRepository;

import java.util.Arrays;

@Configuration
public class InitialDataConfig {

    @Bean
    public ApplicationRunner dataLoader(final IngredientRepository ingredientRepository,
                                        final TacoRepository tacoRepository) {

        return args -> {
            final Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Type.WRAP);
            final Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Type.WRAP);
            final Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Type.PROTEIN);
            final Ingredient carnitas = new Ingredient("CARN", "Carnitas", Type.PROTEIN);
            final Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES);
            final Ingredient lettuce = new Ingredient("LETC", "Lettuce", Type.VEGGIES);
            final Ingredient cheddar = new Ingredient("CHED", "Cheddar", Type.CHEESE);
            final Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Type.CHEESE);
            final Ingredient salsa = new Ingredient("SLSA", "Salsa", Type.SAUCE);
            final Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Type.SAUCE);

            ingredientRepository.save(flourTortilla);
            ingredientRepository.save(cornTortilla);
            ingredientRepository.save(groundBeef);
            ingredientRepository.save(carnitas);
            ingredientRepository.save(tomatoes);
            ingredientRepository.save(lettuce);
            ingredientRepository.save(cheddar);
            ingredientRepository.save(jack);
            ingredientRepository.save(salsa);
            ingredientRepository.save(sourCream);

            final Taco taco1 = new Taco();
            taco1.setName("Carnivore");
            taco1.setIngredients(
                    Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar)
            );
            tacoRepository.save(taco1);

            final Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(
                    Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream)
            );
            tacoRepository.save(taco2);

            final Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(
                    Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa)
            );
            tacoRepository.save(taco3);
        };
    }
}
