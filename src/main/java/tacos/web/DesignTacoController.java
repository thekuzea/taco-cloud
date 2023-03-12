package tacos.web;

import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
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
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.persistence.IngredientRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
@RequiredArgsConstructor
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

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
        final List<Ingredient> ingredients = ingredientRepository.findAll();

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
