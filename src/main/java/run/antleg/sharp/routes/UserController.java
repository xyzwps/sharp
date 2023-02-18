package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.user.UserHandler;
import run.antleg.sharp.modules.user.command.UpsertUserCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.util.Objects;

@Tag(name = "用户信息")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "创建用户")
    @PostMapping
    public User createUser(@RequestBody @Valid UpsertUserCommand cmd) {
        return handler.createUser(cmd);
    }

    @Operation(summary = "获取用户")
    @GetMapping("/{userId}")
    public User findUserById(@Schema(type = "integer", format = "int64", example = "114514")
                           @PathVariable("userId") UserId userId) {
        return handler.findUserById(userId);
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/current")
    @Secured(Roles.ROLE_USER)
    public User findCurrentUser(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return myUserDetails.getUser();
    }

    @Operation(summary = "更新用户")
    @PatchMapping("/{userId}")
    public User updateUser(
            @Schema(type = "integer", format = "int64", example = "114514")
            @PathVariable("userId") UserId userId,
            @RequestBody @Valid UpsertUserCommand cmd) {
        return handler.updateUser(userId, cmd);
    }

    private final UserHandler handler;

    public UserController(UserHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }
}
