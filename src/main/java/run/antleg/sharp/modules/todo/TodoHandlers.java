package run.antleg.sharp.modules.todo;

import jakarta.transaction.Transactional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.todo.command.CreateTodoCommand;
import run.antleg.sharp.modules.todo.command.PatchTodoCommand;
import run.antleg.sharp.modules.todo.model.Todo;
import run.antleg.sharp.modules.todo.model.TodoId;
import run.antleg.sharp.modules.todo.model.TodoService;
import run.antleg.sharp.modules.todo.model.UniverseTodoId;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoHandlers {

    @Secured(Roles.ROLE_USER)
    public List<Todo> get(MyUserDetails userDetails) {
        return todoService.findByUser(userDetails.getUser());
    }

    @Secured(Roles.ROLE_USER)
    public Optional<Todo> get(MyUserDetails userDetails, TodoId todoId) {
        return todoService.findById(UniverseTodoId.builder().id(todoId).userId(userDetails.getUserId()).build());
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    public Todo create(MyUserDetails userDetails, CreateTodoCommand cmd) {
        return todoService.create(Todo.builder()
                        .id(cmd.getTodoId())
                        .userId(userDetails.getUserId())
                        .status(cmd.getStatus())
                        .details(cmd.getDetails())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build());
    }

    @Secured(Roles.ROLE_USER)
    @Transactional
    public Todo patch(MyUserDetails userDetails, TodoId todoId, PatchTodoCommand cmd) {
        return todoService.update(Todo.builder()
                        .id(todoId)
                        .userId(userDetails.getUserId())
                        .status(cmd.getStatus())
                        .details(cmd.getDetails())
                        .build());
    }

    private final TodoService todoService;

    public TodoHandlers(TodoService todoService) {
        this.todoService = Objects.requireNonNull(todoService);
    }
}
