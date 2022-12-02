package pl.spkteam.worklifeintegrationserver.task.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartTimeBeforeEndTimeValidator.class)
public @interface StartTimeBeforeEndTime {
}
