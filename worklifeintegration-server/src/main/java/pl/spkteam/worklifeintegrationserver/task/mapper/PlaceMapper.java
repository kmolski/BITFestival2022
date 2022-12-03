package pl.spkteam.worklifeintegrationserver.task.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.spkteam.worklifeintegrationserver.task.dto.PlaceDto;
import pl.spkteam.worklifeintegrationserver.task.model.Place;
import pl.spkteam.worklifeintegrationserver.task.repo.PlaceRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlaceMapper {

    private final PlaceRepository placeRepository;

    public Place mapPlaceDtoToEntity(PlaceDto placeDto) {
        var entity = Optional.ofNullable(placeDto.getId()).flatMap(placeRepository::findById).orElseGet(Place::new);
        return entity.setName(placeDto.getName())
                     .setTransportTimeMinutes(placeDto.getTransportTimeMinutes());
    }

    public PlaceDto mapPlaceEntityToDto(Place place) {
        return new PlaceDto(
                place.getId(),
                place.getName(),
                place.getTransportTimeMinutes()
        );
    }
}
