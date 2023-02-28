package run.antleg.sharp.modules.tag.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static run.antleg.sharp.modules.Facts.*;

@Data
public class CreateTagCommand {
    @NotNull
    @NotBlank
    @Size(min = TAG_NAME_MIN_LEN, max = TAG_NAME_MAX_LEN)
    String name;
}
