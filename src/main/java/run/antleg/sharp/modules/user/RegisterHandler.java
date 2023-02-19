package run.antleg.sharp.modules.user;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.command.NaiveRegisterCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserService;
import run.antleg.sharp.modules.user.security.MyUserDetails;
import run.antleg.sharp.modules.user.security.MyUserDetailsService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class RegisterHandler {

    private static final LocalDateTime FOREVER = LocalDateTime.of(2222, 12, 12, 0, 0, 0);

    @Transactional
    public void naiveRegister(NaiveRegisterCommand cmd) {
        var username = cmd.getUsername();
        userService.findUserByUsername(username).ifPresent((_it) -> {
            throw new AppException(Errors.USERNAME_CONFLICT);
        });

        var user = userService.createUser(User.builder()
                .username(username)
                .displayName(username)
                .build());

        var userDetails = MyUserDetails.builder()
                .userId(user.getId())
                .user(user)
                .locked(false)
                .password(passwordEncoder.encode(cmd.getPassword()))
                .passwordExp(FOREVER)
                .build();
        userDetailsService.save(userDetails);
    }

    private final UserService userService;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public RegisterHandler(UserService userService,
                           MyUserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.userService = Objects.requireNonNull(userService);
        this.userDetailsService = Objects.requireNonNull(userDetailsService);
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }
}
