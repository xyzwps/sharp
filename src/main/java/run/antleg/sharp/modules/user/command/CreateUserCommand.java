package run.antleg.sharp.modules.user.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserCommand {
    @Schema(defaultValue = "用户的昵称", example = "武沛")
    @NotNull(message = "Parameter displayName is required.")
    @Size(max = 40, message = "Parameter displayName cannot contain more than 40 characters.")
    private String displayName;
}