package run.antleg.sharp.modules.user.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, UserId> {

    Optional<User> findByUsername(String username);
}
