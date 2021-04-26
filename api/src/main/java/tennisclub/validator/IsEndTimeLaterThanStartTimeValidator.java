package tennisclub.validator;
import org.apache.commons.beanutils.PropertyUtils;
import tennisclub.annotations.IsLaterThan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

public class IsEndTimeLaterThanStartTimeValidator implements ConstraintValidator<IsLaterThan, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final IsLaterThan constraintAnnotation)
    {
        firstFieldName = constraintAnnotation.start();
        secondFieldName = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext constraintValidatorContext){
        try {
            final LocalDateTime startTime = (LocalDateTime) PropertyUtils.getProperty(value, firstFieldName);
            final LocalDateTime endTime = (LocalDateTime) PropertyUtils.getProperty(value, secondFieldName);

            return endTime.isEqual(startTime) || endTime.isAfter(startTime);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return true;
    }
}
