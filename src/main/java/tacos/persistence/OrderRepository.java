package tacos.persistence;

import tacos.domain.TacoOrder;

public interface OrderRepository {

    TacoOrder save(TacoOrder order);
}
