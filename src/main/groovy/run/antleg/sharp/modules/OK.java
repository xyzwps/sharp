package run.antleg.sharp.modules;

import lombok.Getter;

public class OK {
    @Getter
    private final boolean ok = true;

    public static final OK INSTANCE = new OK();
}
