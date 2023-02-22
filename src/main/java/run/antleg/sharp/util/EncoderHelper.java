package run.antleg.sharp.util;

import java.util.Base64;

public final class EncoderHelper {

    public static String base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.getDecoder().decode(str);
    }
}
