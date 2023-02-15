package run.antleg.sharp.config.hibernate;

import run.antleg.sharp.modules.anthology.model.AuthorId;

public class AuthorIdUserType extends LongRecordUserType<AuthorId>{
    public AuthorIdUserType() {
        super(AuthorId.class, AuthorId::new, AuthorId::value);
    }
}
