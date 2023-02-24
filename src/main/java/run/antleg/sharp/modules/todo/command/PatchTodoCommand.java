package run.antleg.sharp.modules.todo.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.config.validation.OneOfStrings;
import run.antleg.sharp.modules.todo.model.TodoStatus;

import static run.antleg.sharp.modules.Facts.TODO_DETAILS_MAX_LEN;

@Data
public class PatchTodoCommand {
    @NotBlank(message = "待办内容不能是空白")
    @Size(max = TODO_DETAILS_MAX_LEN, message = "待办内容不可超过 " + TODO_DETAILS_MAX_LEN + " 个字符")
    private String details;

    @NotNull(message = "缺少 status")
    // TODO: 可以考虑下 i18n
    @OneOfStrings(value = {"TODO", "IN_PROGRESS", "DONE", "DELETED"}, message = "不正确的 status")
    private String status;

    public TodoStatus status() {
        return TodoStatus.valueOf(status);
    }
}
