package run.antleg.sharp.modules.todo;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
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

@Tag(name = "待办")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @GetMapping
    @Secured(Roles.ROLE_USER)
    public List<Todo> get(@AuthenticationPrincipal MyUserDetails userDetails) {
        return todoService.findByUser(userDetails.getUser());
    }

    @GetMapping("/{todoId}")
    @Secured(Roles.ROLE_USER)
    public Todo getById(@AuthenticationPrincipal MyUserDetails userDetails,
                        @PathVariable("todoId") TodoId todoId) {
        var uid = UniverseTodoId.builder()
                .id(todoId)
                .userId(userDetails.getUserId())
                .build();
        return todoService.findById(uid)
                .orElseThrow(() -> new AppException(Errors.TODO_NOT_FOUND));
    }

    @PostMapping
    @Secured(Roles.ROLE_USER)
    @Transactional
    public Todo create(@AuthenticationPrincipal MyUserDetails userDetails,
                       @RequestBody @Valid CreateTodoCommand cmd) {
        return todoService.create(Todo.builder()
                .id(cmd.getTodoId())
                .userId(userDetails.getUserId())
                .status(cmd.status())
                .details(cmd.getDetails())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/{todoId}")
    @Secured(Roles.ROLE_USER)
    @Transactional
    public Todo patch(@AuthenticationPrincipal MyUserDetails userDetails,
                      @PathVariable("todoId") TodoId todoId,
                      @RequestBody @Valid PatchTodoCommand cmd) {
        var uid = UniverseTodoId.builder()
                .id(todoId)
                .userId(userDetails.getUserId())
                .build();
        return todoService.update(uid, cmd);
    }


    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = Objects.requireNonNull(todoService);
    }
}