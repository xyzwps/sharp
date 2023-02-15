package run.antleg.sharp.config.hibernate;

import run.antleg.sharp.modules.anthology.model.AnthologyId;

public class AnthologyIdUserType extends StringRecordUserType<AnthologyId> {
    public AnthologyIdUserType() {
        super(AnthologyId.class, AnthologyId::new, AnthologyId::value);
    }
}
