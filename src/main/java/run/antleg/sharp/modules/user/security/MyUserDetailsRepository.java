package run.antleg.sharp.modules.user.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import run.antleg.sharp.modules.user.model.UserId;

import java.util.Optional;

@Repository
public interface MyUserDetailsRepository extends CrudRepository<MyUserDetails, UserId> {

    @Query("select d from MyUserDetails d where d.user.username = ?1")
    Optional<MyUserDetails> findByUsername(String username);
}
