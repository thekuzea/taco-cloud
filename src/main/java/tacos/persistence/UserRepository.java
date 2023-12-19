package tacos.persistence;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}
