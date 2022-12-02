package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Transactional
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("from task where start_time < :end and end_time > :start")
    List<Task> findInTimeInterval(Instant start, Instant end);
}
