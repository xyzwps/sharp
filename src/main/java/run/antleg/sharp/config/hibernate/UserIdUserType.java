package run.antleg.sharp.config.hibernate;

import run.antleg.sharp.modules.user.model.UserId;

public class UserIdUserType extends LongRecordUserType<UserId> {

    public UserIdUserType() {
        super(UserId.class, UserId::new, UserId::value);
    }
}