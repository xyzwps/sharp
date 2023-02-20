package run.antleg.sharp.modules.todo.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.todo.model.TodoId;
import run.antleg.sharp.modules.todo.model.TodoStatus;

@Data
public class CreateTodoCommand {
    @NotNull
    @Size(min = 14, max = 24)
    private TodoId todoId;

    @NotBlank
    @Size(max = 120)
    private String details;

    @NotNull
    private TodoStatus status;
}
