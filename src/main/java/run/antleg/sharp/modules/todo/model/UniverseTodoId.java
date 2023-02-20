package run.antleg.sharp.modules.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import run.antleg.sharp.modules.user.model.UserId;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniverseTodoId implements Serializable {
    private TodoId id;
    private UserId userId;
}
