package run.antleg.sharp.modules.tag.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTagCommand {
    @NotNull
    @NotBlank
    String name;
}
