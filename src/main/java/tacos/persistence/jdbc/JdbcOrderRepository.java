package tacos.persistence.jdbc;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import tacos.domain.IngredientRef;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.persistence.OrderRepository;

@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository implements OrderRepository {

    private static final String INSERT_TACO_ORDER_SQL = "INSERT INTO Taco_Order "
            + "(delivery_name, delivery_street, delivery_city, "
            + "delivery_state, delivery_zip, cc_number, "
            + "cc_expiration, cc_cvv, placed_at) "
            + "VALUES (?,?,?,?,?,?,?,?,?)";

    private static final String INSERT_TACO_SQL = "INSERT INTO Taco "
            + "(name, created_at, taco_order, taco_order_key) "
            + "VALUES (?, ?, ?, ?)";

    private static final String INSERT_INGREDIENT_REF_SQL = "INSERT INTO Ingredient_Ref " +
            "(ingredient, taco, taco_key) "
            + "VALUES (?, ?, ?)";

    private final JdbcOperations jdbcOperations;

    @Override
    public TacoOrder save(final TacoOrder order) {
        order.setPlacedAt(new Date());

        final int[] parameterTypes = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.TIMESTAMP
        };

        final PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(INSERT_TACO_ORDER_SQL, parameterTypes);

        pscf.setReturnGeneratedKeys(true);

        final List<Object> parameters = Arrays.asList(
                order.getDeliveryName(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getDeliveryState(),
                order.getDeliveryZip(),
                order.getCcNumber(),
                order.getCcExpiration(),
                order.getCcCVV(),
                order.getPlacedAt()
        );

        final PreparedStatementCreator psc = pscf.newPreparedStatementCreator(parameters);

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        final long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        order.setId(orderId);

        int orderKey = 0;
        for (Taco taco : order.getTacos()) {
            saveTaco(orderId, orderKey++, taco);
        }

        return order;
    }

    private void saveTaco(final Long orderId, final int orderKey, final Taco taco) {
        taco.setCreatedAt(new Date());

        final int[] parameterTypes = new int[]{Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG};

        final PreparedStatementCreatorFactory pscf =
                new PreparedStatementCreatorFactory(INSERT_TACO_SQL, parameterTypes);

        pscf.setReturnGeneratedKeys(true);

        final List<Object> parameters = Arrays.asList(taco.getName(), taco.getCreatedAt(), orderId, orderKey);
        final PreparedStatementCreator psc = pscf.newPreparedStatementCreator(parameters);

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        final long tacoId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());
    }

    private void saveIngredientRefs(final long tacoId, final List<IngredientRef> ingredientRefs) {
        int tacoKey = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update(INSERT_INGREDIENT_REF_SQL, ingredientRef.getIngredient(), tacoId, tacoKey++);
        }
    }
}
