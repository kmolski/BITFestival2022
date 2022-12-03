package pl.spkteam.worklifeintegrationserver.task.api;

import java.time.LocalDateTime;

public interface TimeIntervalObject {

    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
}
