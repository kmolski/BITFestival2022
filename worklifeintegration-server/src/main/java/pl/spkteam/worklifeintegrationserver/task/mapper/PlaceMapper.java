package pl.spkteam.worklifeintegrationserver.task.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.spkteam.worklifeintegrationserver.task.dto.PlaceDto;
import pl.spkteam.worklifeintegrationserver.task.model.Place;

@Component
@RequiredArgsConstructor
public class PlaceMapper {

    public Place mapPlaceDtoToEntity(PlaceDto placeDto) {
        return new Place(
                placeDto.getId(),
                placeDto.getName(),
                placeDto.getTransportTimeMinutes()
        );
    }

    public PlaceDto mapPlaceEntityToDto(Place place) {
        return new PlaceDto(
                place.getId(),
                place.getName(),
                place.getTransportTimeMinutes()
        );
    }
}
