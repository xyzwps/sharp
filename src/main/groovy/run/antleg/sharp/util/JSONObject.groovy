package run.antleg.sharp.util

class JSONObject extends HashMap<String, Object> {

    Boolean getBoolean(String key) {
        def value = this[key]
        if (value == null) return null
        if (value instanceof Boolean) return value
        throw new InvalidJSONValueException("Invalid bool value of property $key")
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
