package run.antleg.sharp.util


class JSONObject extends HashMap<String, Object> {

    Boolean getBoolean(String key) {
        def value = this[key]
        if (value == null) return null
        if (value instanceof Boolean) return value
        throw new InvalidJSONValueException("Invalid bool value of property $key")
    }

    Long getLong(String key) {
        def value = this[key]
        return switch (value) {
            case null -> null
            case Short -> value.toLong()
            case Integer -> value.toLong()
            case Long -> value
            case BigInteger -> value.toLong()
            default -> throw new InvalidJSONValueException("Invalid long value of property $key")
        }
    }

    String getString(String key) {
        def value = this[key]
        if (value == null) return null
        if (value instanceof String) return value
        throw new InvalidJSONValueException("Invalid string value of property $key")
    }


    static class InvalidJSONValueException extends RuntimeException {
        InvalidJSONValueException(String message) {
            super(message)
        }
    }
}
