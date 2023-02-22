package run.antleg.sharp.modules.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.Facts;
import run.antleg.sharp.modules.OK;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.command.NaiveRegisterCommand;
import run.antleg.sharp.modules.user.model.User;
import run.antleg.sharp.modules.user.model.UserService;
import run.antleg.sharp.modules.user.security.MyUserDetails;
import run.antleg.sharp.modules.user.security.MyUserDetailsService;

import java.time.LocalDateTime;
import java.util.Objects;

@Tag(name = "Action")
@RestController
@RequestMapping("/api")
public class RegisterController {

    // TODO: 防止恶意注册

    @Operation(summary = "简单注册")
    @PostMapping("/register/naive")
    @Transactional
    public OK naiveRegister(@RequestBody @Valid NaiveRegisterCommand cmd) {
        var username = cmd.getUsername();
        userService.findUserByUsername(username).ifPresent((_it) -> {
            throw new AppException(Errors.USERNAME_CONFLICT);
        });

        var user = userService.createUser(User.builder()
                .username(username)
                .displayName(username)
                .registerTime(LocalDateTime.now())
                .build());

        var userDetails = MyUserDetails.builder()
                .userId(user.getId())
                .user(user)
                .locked(false)
                .password(passwordEncoder.encode(cmd.getPassword()))
                .passwordExp(Facts.FOREVER)
                .build();
        userDetailsService.save(userDetails);
        return OK.INSTANCE;
    }

    private final UserService userService;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService,
                              MyUserDetailsService userDetailsService,
                              PasswordEncoder passwordEncoder) {
        this.userService = Objects.requireNonNull(userService);
        this.userDetailsService = Objects.requireNonNull(userDetailsService);
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }
}
