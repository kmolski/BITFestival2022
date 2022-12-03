package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

@Transactional
public interface TaskRepository extends ListCrudRepository<Task, Long>, TaskRepositoryExt {
}
