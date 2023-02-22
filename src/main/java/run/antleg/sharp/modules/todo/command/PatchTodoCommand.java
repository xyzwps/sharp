package run.antleg.sharp.modules.todo.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.todo.model.TodoStatus;

@Data
public class PatchTodoCommand {
    @NotBlank
    @Size(max = 120)
    private String details;

    @NotNull
    private TodoStatus status;
}
