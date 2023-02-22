package run.antleg.sharp.modules;

import lombok.Getter;
import lombok.Setter;

public class OK {
    @Getter
    @Setter
    private boolean ok = true;

    public static final OK INSTANCE = new OK();
}
