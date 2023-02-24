package run.antleg.sharp.config.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;


public class OneOfStringsValidator extends OneOfValidator<OneOfStrings, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || super.isValid(value, annotation.value(), StringUtils::equals, context);
    }
}