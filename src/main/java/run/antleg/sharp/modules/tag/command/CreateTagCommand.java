package run.antleg.sharp.modules.tag.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static run.antleg.sharp.modules.Facts.*;

@Data
public class CreateTagCommand {
    // TODO: 对合法字符做检查
    @NotBlank(message = "标签名不能是空白")
    @Size( max = TAG_NAME_MAX_LEN,
            message = "标签名最多包含 " + TAG_NAME_MAX_LEN + " 个字符")
    String name;
}
