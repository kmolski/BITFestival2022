package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.repository.CrudRepository;
import pl.spkteam.worklifeintegrationserver.task.model.PlacementLimit;

public interface PlacementLimitRepository extends CrudRepository<PlacementLimit, Long> {
}
