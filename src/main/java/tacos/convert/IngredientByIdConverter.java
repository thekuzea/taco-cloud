package tacos.convert;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.domain.udt.IngredientUDT;
import tacos.domain.utils.TacoUDRUtils;
import tacos.persistence.IngredientRepository;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, IngredientUDT> {

    private final IngredientRepository ingredientRepository;

    @Override
    public IngredientUDT convert(String id) {
        return ingredientRepository.findById(id)
                .map(TacoUDRUtils::toIngredientUDT)
                .orElse(null);
    }
}
