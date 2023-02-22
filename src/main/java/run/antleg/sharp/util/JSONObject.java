package run.antleg.sharp.util;


import java.math.BigInteger;
import java.util.HashMap;

public class JSONObject extends HashMap<String, Object> {

    public Boolean getBoolean(String key) {
        return switch (this.get(key)) {
            case null -> null;
            case Boolean it -> it;
            default -> throw new InvalidJSONValueException("Invalid bool value of property " + key);
        };
    }

    public Long getLong(String key) {
        return switch (this.get(key)) {
            case null -> null;
            case Short it -> it.longValue();
            case Integer it -> it.longValue();
            case Long it -> it;
            case BigInteger it -> it.longValue();
            default -> throw new InvalidJSONValueException("Invalid long value of property " + key);
        };
    }

    public Integer getInteger(String key) {
        return switch (this.get(key)) {
            case null -> null;
            case Short it -> it.intValue();
            case Integer it -> it;
            case Long it -> it.intValue();
            case BigInteger it -> it.intValue();
            default -> throw new InvalidJSONValueException("Invalid integer value of property " + key);
        };
    }

    public String getString(String key) {
        return switch (this.get(key)) {
            case null -> null;
            case String it -> it;
            default -> throw new InvalidJSONValueException("Invalid string value of property " + key);
        };
    }


    public static class InvalidJSONValueException extends RuntimeException {
        private InvalidJSONValueException(String message) {
            super(message);
        }
    }
}
