package tacos.web;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tacos.domain.Ingredient;
import tacos.domain.Ingredient.Type;
import tacos.domain.TacoOrder;
import tacos.domain.Taco;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute
    public void addIngredientToModel(final Model model) {
        final List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Arrays.stream(Ingredient.Type.values())
                .forEach(type -> model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type)));
    }

    private List<Ingredient> filterByType(final List<Ingredient> ingredients, final Ingredient.Type type) {
        return ingredients.stream()
                .filter(ingredient -> type == ingredient.getType())
                .toList();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid final Taco taco,
                              final Errors errors,
                              @ModelAttribute final TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        log.info("Processing taco: {}", taco);
        tacoOrder.addTaco(taco);

        return "redirect:/orders/current";
    }
}
