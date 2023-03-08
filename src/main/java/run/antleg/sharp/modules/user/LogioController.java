package run.antleg.sharp.modules.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import run.antleg.sharp.config.security.JwtService;
import run.antleg.sharp.config.security.LoginPayload;
import run.antleg.sharp.config.security.SecurityDicts;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserService;

@Tag(name = "Action")
@RestController
public class LogioController {

    /**
     * 具体实现见 {@link run.antleg.sharp.config.security.RestLoginAuthenticationFilter}
     */
    @Operation(summary = "简单登录")
    @PostMapping(SecurityDicts.LOGIN_REQUEST_URL)
    public User restLogin(@RequestBody LoginPayload ignored) {
        throw new RuntimeException("是假的啦，仅用于文档生成");
    }

    /**
     * 具体实现见 {@link run.antleg.sharp.config.security.WebSecurityConfig#securityFilterChain}
     */
    @Operation(summary = "退出登录", responses = @ApiResponse(responseCode = "204"))
    @PostMapping(SecurityDicts.LOGOUT_REQUEST_URL)
    public void logout() {
        throw new RuntimeException("是假的啦，仅用于文档生成");
    }
}
