package run.antleg.sharp.modules.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.antleg.sharp.modules.user.command.CreateUserCommand;
import run.antleg.sharp.modules.user.model.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserHandler handler;

    @PostMapping("/create-user")
    public User createUser(@RequestBody @Valid CreateUserCommand cmd) {
        return handler.createUser(cmd);
    }
}
