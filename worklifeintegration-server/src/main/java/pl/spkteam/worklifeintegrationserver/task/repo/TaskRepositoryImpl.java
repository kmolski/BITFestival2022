package pl.spkteam.worklifeintegrationserver.task.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryExt {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> findInTimeInterval(LocalDateTime start, LocalDateTime end) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(Task.class);
        var task = query.from(Task.class);

        var predicates = new ArrayList<Predicate>();
        if (start != null) {
            predicates.add(cb.lessThan(task.get("startTime"), end));
        }
        if (end != null) {
            predicates.add(cb.greaterThan(task.get("endTime"), start));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
