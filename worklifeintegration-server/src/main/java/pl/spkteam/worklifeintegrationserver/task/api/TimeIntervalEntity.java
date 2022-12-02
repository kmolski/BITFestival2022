package pl.spkteam.worklifeintegrationserver.task.api;

import java.time.LocalDateTime;

public interface TimeIntervalEntity {

    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
}
