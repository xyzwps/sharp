package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.antleg.sharp.modules.user.UserHandler;
import run.antleg.sharp.modules.user.command.CreateUserCommand;
import run.antleg.sharp.modules.user.model.User;

import java.util.Objects;

@Tag(name = "用户信息")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "创建用户")
    @PostMapping
    public User createUser(@RequestBody @Valid CreateUserCommand cmd) {
        return handler.createUser(cmd);
    }

    private final UserHandler handler;

    public UserController(UserHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }
}
