package run.antleg.sharp.modules.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(type = "string")
public record TodoId(String value) implements Serializable, CharSequence {

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }
}
