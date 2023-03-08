package run.antleg.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[\\w-]+$");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    protected Username annotation;

    @Override
    public void initialize(Username constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (DIGITS_PATTERN.matcher(value).matches()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(value).matches();
    }

    public boolean isValid(String value, String[] possibleValues, BiFunction<String, String, Boolean> equalsMethod, ConstraintValidatorContext context) {
        for (String possibleVal : possibleValues) {
            if (equalsMethod.apply(value, possibleVal)) {
                return true;
            }
        }
        return false;
    }
}