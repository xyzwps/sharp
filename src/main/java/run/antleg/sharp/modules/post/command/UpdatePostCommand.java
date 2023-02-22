package run.antleg.sharp.modules.post.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePostCommand {
    @NotEmpty
    @Size(max = 200)
    private String title;
    @NotEmpty
    private String content;
}
