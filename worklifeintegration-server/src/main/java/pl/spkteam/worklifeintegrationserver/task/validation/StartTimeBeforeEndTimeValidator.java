package pl.spkteam.worklifeintegrationserver.task.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.spkteam.worklifeintegrationserver.task.api.TimeIntervalObject;

public class StartTimeBeforeEndTimeValidator implements ConstraintValidator<StartTimeBeforeEndTime, TimeIntervalObject> {

    @Override
    public boolean isValid(TimeIntervalObject value, ConstraintValidatorContext context) {
        return value.getStartTime().isBefore(value.getEndTime());
    }
}
