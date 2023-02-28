package run.antleg.sharp.test.util;

import org.apache.commons.lang3.RandomStringUtils;

import static run.antleg.sharp.modules.Facts.*;

public class TagUtils {

    public static String randomTagName() {
        return RandomStringUtils.randomAlphanumeric(PASSWORD_MIN_LEN, PASSWORD_MAX_LEN);
    }

    public static String randomPasswordMayBeInvalid(int len) {
        return RandomStringUtils.randomAlphanumeric(len);
    }
}
