package run.antleg.sharp.modules.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.command.UpsertUserCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;
import run.antleg.sharp.modules.user.model.UserService;

import java.util.Objects;

@Service
public class UserHandler {

    public User createUser(UpsertUserCommand cmd) {
        var newUser = new User();
        cmd.updateUser(newUser);
        return userService.createUser(newUser);
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
