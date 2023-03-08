package run.antleg.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Null value is allowed. You should use {@link jakarta.validation.constraints.NotNull} or
 * {@link jakarta.validation.constraints.NotEmpty} to check null value.
 */
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface Username {
    String message() default "{misc.username}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
