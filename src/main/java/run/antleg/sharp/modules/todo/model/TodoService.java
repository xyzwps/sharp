package run.antleg.sharp.modules.todo.model;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.todo.command.PatchTodoCommand;
import run.antleg.sharp.modules.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * FIXME: 没考虑有人创建几十万个 todo 的情况
 */
@Service
public class TodoService {

    public Todo create(Todo todo) {
        em.persist(todo); // TODO: 尝试使用 jpa repository 来移除对 em 的使用
        return todoRepository.findById(todo.universeTodoId())
                .orElseThrow(() -> new AppException(Errors.IMPOSSIBLE));
    }

    public Todo update(UniverseTodoId todoId, PatchTodoCommand cmd) {
        var todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new AppException(Errors.TODO_NOT_FOUND));
        todo.setDetails(cmd.getDetails());
        todo.setStatus(cmd.status());
        todo.setUpdateTime(LocalDateTime.now());
        return todoRepository.save(todo);
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