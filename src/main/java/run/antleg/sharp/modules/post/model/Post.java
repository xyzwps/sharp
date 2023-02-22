package run.antleg.sharp.modules.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.PostIdUserType;
import run.antleg.sharp.config.hibernate.UserIdUserType;
import run.antleg.sharp.modules.user.model.UserId;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(PostIdUserType.class)
    private PostId id;
    @Type(UserIdUserType.class)
    private UserId userId;
    private String title;
    @JsonIgnore
    private String idem;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @OneToOne
    @PrimaryKeyJoinColumn
    private PostContent content;
}
