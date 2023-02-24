package run.antleg.validation;

import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class OneOfStringsValidator extends OneOfValidator<OneOfStrings, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || super.isValid(value, annotation.value(), Objects::equals, context);
    }
}