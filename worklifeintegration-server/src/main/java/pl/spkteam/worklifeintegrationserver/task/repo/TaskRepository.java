package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("from Task where (startTime is null or startTime < :end) or (endTime is null or endTime > :start)")
    List<Task> findInTimeInterval(LocalDateTime start, LocalDateTime end);
}
