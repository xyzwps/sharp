package run.antleg.sharp.modules.user.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.user.model.User;

@Data
public class PatchUserCommand {

    @Schema(description = "用户的昵称", example = "武沛")
    @NotBlank(message = "缺少昵称")
    @Size(max = 40, message = "昵称不可超过 40 个字符")
    private String displayName;

    public void updateUser(User user) {
        user.setDisplayName(this.displayName);
    }
}
