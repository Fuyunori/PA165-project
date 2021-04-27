package tennisclub.validators;
import org.apache.commons.beanutils.PropertyUtils;
import tennisclub.annotations.IsEndTimeAfterStartTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

public class IsEndAfterStartValidator implements ConstraintValidator<IsEndTimeAfterStartTime, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final IsEndTimeAfterStartTime constraintAnnotation)
    {
        firstFieldName = constraintAnnotation.start();
        secondFieldName = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(final Object annotatedObject, final ConstraintValidatorContext constraintValidatorContext){
        try {
            final LocalDateTime startTime = (LocalDateTime) PropertyUtils.getProperty(annotatedObject, firstFieldName);
            final LocalDateTime endTime = (LocalDateTime) PropertyUtils.getProperty(annotatedObject, secondFieldName);

            return endTime.isEqual(startTime) || endTime.isAfter(startTime);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error while reading startTime or endTime on class " + annotatedObject.getClass().getName() );
        }
    }
}
