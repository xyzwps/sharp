package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.user.UserHandler;
import run.antleg.sharp.modules.user.command.CreateUserCommand;
import run.antleg.sharp.modules.user.command.UpdateUserCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;

import java.util.Objects;

@SuppressWarnings("unused")
@Tag(name = "用户信息")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "创建用户")
    @PostMapping
    public User createUser(@RequestBody @Valid CreateUserCommand cmd) {
        return handler.createUser(cmd);
    }

    @Operation(summary = "创建用户")
    @PostMapping("/{userId}")
    public User updateUser(
            @Schema(type = "integer", format = "int64", example = "114514")
            @PathVariable("userId") UserId userId,
            @RequestBody @Valid UpdateUserCommand cmd) {
        return handler.updateUser(userId, cmd);
    }

    private final UserHandler handler;

    public UserController(UserHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }
}
