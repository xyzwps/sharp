package run.antleg.sharp.modules;

import java.time.LocalDateTime;

public final class Facts {
    public static final int USERNAME_MAX_LEN = 24;
    public static final int USERNAME_MIN_LEN = 1;

    public static final int PASSWORD_MAX_LEN = 32;
    public static final int PASSWORD_MIN_LEN = 8;

    public static final LocalDateTime FOREVER = LocalDateTime.of(2345, 12, 12, 0, 0, 0);
}
