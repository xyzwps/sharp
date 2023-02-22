package run.antleg.sharp.modules.todo.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import run.antleg.sharp.modules.user.model.UserId;

import java.util.List;

@Repository
public interface TodoRepository extends CrudRepository<Todo, UniverseTodoId> {

    List<Todo> findByUserId(UserId userId);
}
