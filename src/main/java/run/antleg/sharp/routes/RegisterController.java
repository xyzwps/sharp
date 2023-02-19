package run.antleg.sharp.routes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import run.antleg.sharp.modules.OK;
import run.antleg.sharp.modules.user.RegisterHandler;
import run.antleg.sharp.modules.user.command.NaiveRegisterCommand;

import java.util.Objects;

@Tag(name = "Action")
@RestController
@RequestMapping("/api")
public class RegisterController {

    // TODO: 防止恶意注册

    @Operation(summary = "简单注册")
    @PostMapping("/register/naive")
    public OK updateUser(@RequestBody @Valid NaiveRegisterCommand cmd) {
        handler.naiveRegister(cmd);
        return OK.INSTANCE;
    }

    private final RegisterHandler handler;

    public RegisterController(RegisterHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }
}
