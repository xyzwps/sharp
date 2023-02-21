package run.antleg.sharp.modules.user.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NaiveRegisterCommand {
    @Schema(description = "用户名", example = "diona")
    @NotNull(message = "Parameter username is required.") // TODO: 限制可用字符
    @Size(max = 24)
    private String username;

    @Schema(description = "密码", example = "cool-password")
    @NotNull(message = "Parameter password is required.")
    @Size(min = 5, max = 32)
    private String password;

}
