package run.antleg.sharp.modules.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.UserIdUserType;

import java.time.LocalDateTime;


@Entity(name = "User")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(UserIdUserType.class)
    @Schema(type = "integer", example = "114514")
    private UserId id;

    @Schema(example = "diona")
    private String username;

    @Schema(example = "文渊")
    private String displayName;

    @Column(name = "created_at")
    private LocalDateTime registerTime;
}