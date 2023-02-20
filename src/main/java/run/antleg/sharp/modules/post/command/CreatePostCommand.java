package run.antleg.sharp.modules.post.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.post.model.PostType;

@Data
public class CreatePostCommand {
    @NotNull
    private PostType type;
    @NotEmpty
    @Size(max = 200)
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    @Size(min = 24, max = 48)
    private String idem; // TODO: 支持去重
}
