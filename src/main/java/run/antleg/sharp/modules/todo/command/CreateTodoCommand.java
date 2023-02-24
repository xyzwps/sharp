package run.antleg.sharp.modules.todo.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import run.antleg.sharp.modules.todo.model.TodoId;
import run.antleg.sharp.modules.todo.model.TodoStatus;
import run.antleg.validation.OneOfStrings;

import static run.antleg.sharp.modules.Facts.*;

@Data
public class CreateTodoCommand {
    @NotNull(message = "缺少 todoId")
    @Size(min = TODO_ID_MIN_LEN, max = TODO_ID_MAX_LEN,
            message = "TodoId 应包含 " + TODO_ID_MIN_LEN + " 到 " + TODO_ID_MAX_LEN + " 个字符")
    @Pattern(regexp = "^[A-Za-z0-9_.\\-]*$", message = "TodoId 只能包含大小写字母、数字和 '_'、'-'、'.'")
    private TodoId todoId;

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
