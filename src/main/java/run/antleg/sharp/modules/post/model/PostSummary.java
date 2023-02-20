package run.antleg.sharp.modules.post.model;

import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.PostIdUserType;
import run.antleg.sharp.config.hibernate.UserIdUserType;
import run.antleg.sharp.modules.user.model.UserId;

import java.time.LocalDateTime;

public interface PostSummary {
    @Type(PostIdUserType.class)
    PostId getId();

    @Type(UserIdUserType.class)
    UserId getUserId();

    String getTitle();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();
}
