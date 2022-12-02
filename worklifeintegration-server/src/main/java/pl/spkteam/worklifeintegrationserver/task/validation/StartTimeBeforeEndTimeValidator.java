package pl.spkteam.worklifeintegrationserver.task.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.spkteam.worklifeintegrationserver.task.api.TimeIntervalEntity;

public class StartTimeBeforeEndTimeValidator implements ConstraintValidator<StartTimeBeforeEndTime, TimeIntervalEntity> {

    @Override
    public boolean isValid(TimeIntervalEntity value, ConstraintValidatorContext context) {
        return value.getStartTime().isBefore(value.getEndTime());
    }
}
