package run.antleg.sharp.test.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;

import static run.antleg.sharp.modules.Facts.*;

public class UsernameUtils {

    private static final Set<String> usedUsernames = new HashSet<>();

    public static String randomUsername() {
        while (true) {
            var u = RandomStringUtils.randomAlphanumeric(USERNAME_MIN_LEN, USERNAME_MAX_LEN);
            if (!usedUsernames.contains(u)) {
                usedUsernames.add(u);
                return u;
            }
        }
    }

    public static String randomUsernameMayBeInvalid(int len) {
        while (true) {
            var u = RandomStringUtils.randomAlphanumeric(len);
            if (!usedUsernames.contains(u)) {
                usedUsernames.add(u);
                return u;
            }
        }
    }
}
