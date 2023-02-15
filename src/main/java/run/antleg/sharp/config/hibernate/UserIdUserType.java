package run.antleg.sharp.config.hibernate;

import lombok.extern.slf4j.Slf4j;
import run.antleg.sharp.modules.user.model.UserId;

@Slf4j
public class UserIdUserType extends LongRecordUserType<UserId> {

    public UserIdUserType() {
        super(UserId.class, UserId::new, UserId::value);
    }
}