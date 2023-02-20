package run.antleg.sharp.modules.user.model;

import org.hibernate.annotations.Type;
import run.antleg.sharp.config.hibernate.UserIdUserType;

public interface UserSummary {
    @Type(UserIdUserType.class)
    UserId getId();

    String getUsername(); // FIXME: username 和 id 选一个

    String getDisplayName();
}
