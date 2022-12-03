package pl.spkteam.worklifeintegrationserver.task.repo;

import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepositoryExt {
    List<Task> findInTimeInterval(LocalDateTime start, LocalDateTime end);
}
