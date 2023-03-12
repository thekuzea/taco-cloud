package tacos.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import tacos.domain.Ingredient;
import tacos.persistence.IngredientRepository;

@Repository
@RequiredArgsConstructor
public class JdbcIngredientRepository implements IngredientRepository {

    private static final String SELECT_ALL_INGREDIENTS_SQL = "SELECT id, name, type FROM Ingredient";

    private static final String SELECT_INGREDIENT_BY_ID_SQL = "SELECT id, name, type FROM Ingredient WHERE id = ?";

    private static final String INSERT_INGREDIENT_SQL = "INSERT INTO Ingredient (id, name, type) VALUES (?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Ingredient> findAll() {
        return jdbcTemplate.query(SELECT_ALL_INGREDIENTS_SQL, this::mapRowToIngredient);
    }

    @Override
    public Optional<Ingredient> findById(final String id) {
        final List<Ingredient> ingredients = jdbcTemplate.query(
                SELECT_INGREDIENT_BY_ID_SQL,
                this::mapRowToIngredient,
                id
        );

        return CollectionUtils.isEmpty(ingredients) ?
                Optional.empty() :
                Optional.of(ingredients.get(0));
    }

    @Override
    public Ingredient save(final Ingredient ingredient) {
        jdbcTemplate.update(
                INSERT_INGREDIENT_SQL,
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString()
        );

        return ingredient;
    }

    private Ingredient mapRowToIngredient(final ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type"))
        );
    }
}
