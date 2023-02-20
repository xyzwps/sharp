package run.antleg.sharp.config.security

import jakarta.validation.constraints.NotNull

class LoginPayload {
    @NotNull(message = "Parameter username is required.")
    String username

    @NotNull(message = "Parameter password is required.")
    String password
}
