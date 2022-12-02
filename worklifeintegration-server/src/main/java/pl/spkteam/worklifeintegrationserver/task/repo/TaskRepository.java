package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.time.Instant;
import java.util.List;

@Transactional
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("from Task where startTime < :end or endTime > :start")
    List<Task> findInTimeInterval(Instant start, Instant end);
}
