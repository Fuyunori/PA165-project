package tennisclub.annotations;

import tennisclub.validators.IsEndAfterStartValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsEndAfterStartValidator.class)
@Documented
public @interface IsEndTimeAfterStartTime {
    String message() default "End date must be greater or equal to start date.";
    Class<?>[] groups() default {};
    String start();

    String end();
}
