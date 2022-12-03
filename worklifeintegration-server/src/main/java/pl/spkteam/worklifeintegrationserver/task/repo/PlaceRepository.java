package pl.spkteam.worklifeintegrationserver.task.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.task.model.Place;

@Transactional
public interface PlaceRepository extends CrudRepository<Place, Long> {
}
