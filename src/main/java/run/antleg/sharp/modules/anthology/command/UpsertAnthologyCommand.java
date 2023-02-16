package run.antleg.sharp.modules.anthology.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.anthology.model.AuthorId;

@Data
public class UpsertAnthologyCommand {
    @NotNull(message = "缺少参数 title")
    @NotBlank(message = "参数 title 不能是空白")
    @Size(max = 100, message = "")
    private String title;

    @NotNull(message = "缺少参数 authorId")
    private AuthorId authorId; // TODO: 该从 session 中获取信息
}
