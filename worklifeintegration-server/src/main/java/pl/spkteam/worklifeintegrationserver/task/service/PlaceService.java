package pl.spkteam.worklifeintegrationserver.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spkteam.worklifeintegrationserver.restapi.exception.EntityNotFoundException;
import pl.spkteam.worklifeintegrationserver.task.model.Place;
import pl.spkteam.worklifeintegrationserver.task.repo.PlaceRepository;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException(placeId, Place.class));
    }
}
