package run.antleg.sharp.modules.todo.model;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoService {

    public Todo create(Todo todo) {
        em.persist(todo);
        return todoRepository.findById(todo.universeTodoId())
                .orElseThrow(() -> new AppException(Errors.IMPOSSIBLE));
    }

    public Todo update(Todo todo) {
        var oldTodo = todoRepository.findById(todo.universeTodoId())
                .orElseThrow(() -> new AppException(Errors.TODO_NOT_FOUND));
        oldTodo.setDetails(todo.getDetails());
        oldTodo.setStatus(todo.getStatus());
        oldTodo.setUpdateTime(LocalDateTime.now());
        return todoRepository.save(oldTodo);
    }

    public List<Todo> findByUser(User user) {
        return todoRepository.findByUserId(user.getId());
    }

    public Optional<Todo> findById(UniverseTodoId utid) {
        return todoRepository.findById(utid);
    }

    private final TodoRepository todoRepository;

    private final EntityManager em;

    public TodoService(TodoRepository todoRepository, EntityManager em) {
        this.todoRepository = Objects.requireNonNull(todoRepository);
        this.em = Objects.requireNonNull(em);
    }
}