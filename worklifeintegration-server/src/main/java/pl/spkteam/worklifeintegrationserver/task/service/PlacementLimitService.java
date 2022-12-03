package pl.spkteam.worklifeintegrationserver.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spkteam.worklifeintegrationserver.restapi.exception.EntityNotFoundException;
import pl.spkteam.worklifeintegrationserver.task.model.PlacementLimit;
import pl.spkteam.worklifeintegrationserver.task.repo.PlacementLimitRepository;

@Service
@RequiredArgsConstructor
public class PlacementLimitService {

    private final PlacementLimitRepository placementLimitRepository;

    public PlacementLimit getPlacementLimitById(Long placementLimitId) {
        return placementLimitRepository.findById(placementLimitId)
                .orElseThrow(() -> new EntityNotFoundException(placementLimitId, PlacementLimit.class));
    }
}
