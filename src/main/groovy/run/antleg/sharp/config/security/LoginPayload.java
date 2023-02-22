package run.antleg.sharp.config.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static run.antleg.sharp.modules.Facts.*;

@Data
public class LoginPayload {
    @NotNull(message = "缺少用户名")
    @Size(min = USERNAME_MIN_LEN, max = USERNAME_MAX_LEN,
            message = "用户名长度应在 " + USERNAME_MIN_LEN + " 到 " + USERNAME_MAX_LEN + " 之间")
    private String username;

    @NotNull(message = "缺少密码")
    @Size(min = PASSWORD_MIN_LEN, max = PASSWORD_MAX_LEN,
            message = "密码长度应在 " + PASSWORD_MIN_LEN + " 到 " + PASSWORD_MAX_LEN + " 之间")
    private String password;
}
