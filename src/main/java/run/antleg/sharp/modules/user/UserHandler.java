package run.antleg.sharp.modules.user;

import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.user.command.CreateUserCommand;
import run.antleg.sharp.modules.user.model.User;
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

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

}
