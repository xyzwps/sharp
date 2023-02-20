package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.todo.TodoHandlers;
import run.antleg.sharp.modules.todo.command.CreateTodoCommand;
import run.antleg.sharp.modules.todo.command.PatchTodoCommand;
import run.antleg.sharp.modules.todo.model.Todo;
import run.antleg.sharp.modules.todo.model.TodoId;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.util.List;
import java.util.Objects;

@Tag(name = "待办")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @GetMapping
    public List<Todo> get(@AuthenticationPrincipal MyUserDetails userDetails) {
        return handlers.get(userDetails);
    }

    @GetMapping("/{todoId}")
    public Todo getById(@AuthenticationPrincipal MyUserDetails userDetails,
                        @PathVariable("todoId") TodoId todoId) {
        return handlers.get(userDetails, todoId)
                .orElseThrow(() -> new AppException(Errors.TODO_NOT_FOUND));
    }

    @PostMapping
    public Todo create(@AuthenticationPrincipal MyUserDetails userDetails,
                       @RequestBody @Valid CreateTodoCommand cmd) {
        return handlers.create(userDetails, cmd);
    }

    @PatchMapping("/{todoId}")
    public Todo patch(@AuthenticationPrincipal MyUserDetails userDetails,
                      @PathVariable("todoId") TodoId todoId,
                      @RequestBody @Valid PatchTodoCommand cmd) {
        return handlers.patch(userDetails, todoId, cmd);
    }


    private final TodoHandlers handlers;

    public TodoController(TodoHandlers handlers) {
        this.handlers = Objects.requireNonNull(handlers);
    }
}
