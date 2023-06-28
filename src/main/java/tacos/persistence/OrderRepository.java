package tacos.persistence;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.TacoOrder;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<TacoOrder, UUID> {

}
