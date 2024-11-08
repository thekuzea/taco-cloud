package tacos.persistence;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {

}
