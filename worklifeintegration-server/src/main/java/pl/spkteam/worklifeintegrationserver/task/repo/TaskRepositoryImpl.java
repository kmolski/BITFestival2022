package pl.spkteam.worklifeintegrationserver.task.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> findInTimeInterval(LocalDateTime start, LocalDateTime end) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Task.class);
        var root = query.from(Task.class);

        if (start != null) {
            cb.and(cb.lessThan(root.get("startTime"), start));
        }
        if (end != null) {
            cb.and(cb.greaterThan(root.get("endTime"), end));
        }

        return entityManager.createQuery(query).getResultList();
    }
}
