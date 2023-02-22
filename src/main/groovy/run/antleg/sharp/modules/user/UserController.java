package run.antleg.sharp.modules.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.config.security.Roles;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.command.PatchUserCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;
import run.antleg.sharp.modules.user.model.UserService;
import run.antleg.sharp.modules.user.security.MyUserDetails;

import java.util.Objects;

@Tag(name = "用户信息")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "获取用户")
    @GetMapping("/{userId}")
    public User findUserById(@Schema(type = "integer", format = "int64", example = "114514")
                             @PathVariable("userId") UserId userId) {
        return userService.findUserById(userId)
                .orElseThrow(() -> new AppException(Errors.USER_NOT_FOUND));
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/current")
    @Secured(Roles.ROLE_USER)
    public User findCurrentUser(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return myUserDetails.getUser();
    }

    @Operation(summary = "更新当前登录用户")
    @PatchMapping("/current")
    @Secured(Roles.ROLE_USER)
    public User updateUser(@AuthenticationPrincipal MyUserDetails myUserDetails,
                           @RequestBody @Valid PatchUserCommand cmd) {
        return userService.updateUser(myUserDetails.getUserId(), cmd);
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }
}
