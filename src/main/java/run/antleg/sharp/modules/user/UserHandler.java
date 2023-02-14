package run.antleg.sharp.modules.user;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.command.CreateUserCommand;
import run.antleg.sharp.modules.user.command.UpdateUserCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserId;
import run.antleg.sharp.modules.user.model.UserService;

import java.util.Objects;

@Service
public class UserHandler {

    public User createUser(CreateUserCommand cmd) {
        var newUser = User.builder()
                .displayName(cmd.getDisplayName())
                .build();
        return userService.createUser(newUser);
    }

    @Transactional
    public User updateUser(UserId userId, UpdateUserCommand cmd) {
        return userService.updateUser(userId, cmd);
    }

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

}
