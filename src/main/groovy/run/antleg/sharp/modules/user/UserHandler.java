package run.antleg.sharp.modules.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.command.UpsertUserCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;
import run.antleg.sharp.modules.user.model.UserService;

import java.util.Objects;

@Service
public class UserHandler {

    public User findUserById(UserId userId) {
        return userService.findUserById(userId).orElseThrow(() -> new AppException(Errors.USER_NOT_FOUND));
    }

    @Transactional
    public User updateUser(UserId userId, UpsertUserCommand cmd) {
        return userService.updateUser(userId, cmd);
    }

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

}
