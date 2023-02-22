package run.antleg.sharp.modules.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.TodoIdUserType;
import run.antleg.sharp.config.hibernate.UserIdUserType;
import run.antleg.sharp.modules.user.model.UserId;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UniverseTodoId.class)
public class Todo {
    @Id
    @Type(TodoIdUserType.class)
    private TodoId id;
    @Id
    @Type(UserIdUserType.class)
    @JsonIgnore
    private UserId userId;
    private String  details;
    @Enumerated(EnumType.STRING)
    private TodoStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UniverseTodoId universeTodoId() {
        return UniverseTodoId.builder().id(id).userId(userId).build();
    }
}


