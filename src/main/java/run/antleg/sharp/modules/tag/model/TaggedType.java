package run.antleg.sharp.modules.tag.model;

import java.util.function.Supplier;

public enum TaggedType {
    POST;

    public static TaggedType from(String str, Supplier<RuntimeException> makeException) {
        try {
            return TaggedType.valueOf(str.toUpperCase());
        } catch (Exception ex) {
            throw makeException.get();
        }
    }
}