package run.antleg.sharp.test.util

import org.apache.commons.lang3.RandomStringUtils

import static run.antleg.sharp.modules.Facts.*

class UsernameUtils {

    private static final Set<String> usedUsernames = new HashSet<>()

    static String randomUsername() {
        while (true) {
            var u = RandomStringUtils.randomAlphanumeric(USERNAME_MIN_LEN, USERNAME_MAX_LEN)
            if (u !in usedUsernames) {
                usedUsernames << u
                return u
            }
        }
    }

    static String randomUsernameMayBeInvalid(int len) {
        while (true) {
            var u = RandomStringUtils.randomAlphanumeric(len)
            if (u !in usedUsernames) {
                usedUsernames << u
                return u
            }
        }
    }
}
