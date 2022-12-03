package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.task.model.PlacementLimit;

@Transactional
public interface PlacementLimitRepository extends CrudRepository<PlacementLimit, Long> {
}
