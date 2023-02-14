package run.antleg.sharp.modules.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.UserIdUserType;


@Entity
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(UserIdUserType.class)
    private UserId userId;

    private String displayName;
}