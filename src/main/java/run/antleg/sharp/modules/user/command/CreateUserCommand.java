package run.antleg.sharp.modules.user.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserCommand {
    @NotNull(message = "Parameter displayName is required.")
    @Size(max = 40, message = "Parameter displayName cannot contain more than 40 characters.")
    private String displayName;
}
