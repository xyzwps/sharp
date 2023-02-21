package run.antleg.sharp.modules.user.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.user.model.User;

@Data
public class UpsertUserCommand {

    @Schema(description = "用户的昵称", example = "武沛")
    @NotNull(message = "Parameter displayName is required.")
    @Size(max = 40, message = "Parameter displayName cannot contain more than 40 characters.")
    private String displayName;

    @Schema(description = "用户名", example = "diona")
    @NotNull(message = "Parameter username is required.")
    @Size(max = 24)
    private String username;

    public void updateUser(User user) {
        user.setDisplayName(this.displayName);
        user.setUsername(this.username);
    }
}
