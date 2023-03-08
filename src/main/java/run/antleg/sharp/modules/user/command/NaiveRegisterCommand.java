package run.antleg.sharp.modules.user.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.validation.Username;

import static run.antleg.sharp.modules.Facts.*;

@Data
public class NaiveRegisterCommand {
    @Schema(description = "用户名", example = "diona")
    @NotNull(message = "缺少用户名")
    @Size(min = USERNAME_MIN_LEN, max = USERNAME_MAX_LEN,
            message = "用户名长度应在 " + USERNAME_MIN_LEN + " 到 " + USERNAME_MAX_LEN + " 个字符之间")
    @Username
    private String username;

    @Schema(description = "密码", example = "cool-password")
    @NotNull(message = "缺少密码")
    @Size(min = PASSWORD_MIN_LEN, max = PASSWORD_MAX_LEN,
            message = "密码长度应在 " + PASSWORD_MIN_LEN + " 到 " + PASSWORD_MAX_LEN + " 个字符之间")
    private String password; // TODO: 尝试 passay zxcvbn 或者自己写一个密码验证器
}
