package run.antleg.sharp.test.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static run.antleg.sharp.modules.Facts.*;

public class UsernameUtils {

    private static final Set<String> usedUsernames = new HashSet<>();

    public static String randomUsername() {
        return randomUsername(() -> RandomStringUtils.randomAlphabetic(USERNAME_MIN_LEN, USERNAME_MAX_LEN));
    }

    public static String randomUsernameMayBeInvalid(int len) {
        return randomUsername(() -> RandomStringUtils.randomAlphabetic(len));
    }

    private static String randomUsername(Supplier<String> genUsername) {
        while (true) {
            var u = genUsername.get();
            if (!usedUsernames.contains(u)) {
                usedUsernames.add(u);
                return u;
            }
        }
    }

}
