package run.antleg.sharp.modules;

import java.time.LocalDateTime;

public final class Facts {
    public static final int USERNAME_MAX_LEN = 24;
    public static final int USERNAME_MIN_LEN = 1;

    public static final int PASSWORD_MAX_LEN = 32;
    public static final int PASSWORD_MIN_LEN = 8;

    public static final int TODO_ID_MIN_LEN = 14;
    public static final int TODO_ID_MAX_LEN = 36;
    public static final int TODO_DETAILS_MAX_LEN = 120;

    public static final int TAG_NAME_MIN_LEN = 1;
    public static final int TAG_NAME_MAX_LEN = 24;

    public static final String HEADER_X_REQUEST_ID = "X-Request-Id";

    public static final LocalDateTime FOREVER = LocalDateTime.of(2345, 12, 12, 0, 0, 0);
}
