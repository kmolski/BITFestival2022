package pl.spkteam.worklifeintegrationserver.task.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.spkteam.worklifeintegrationserver.task.model.PlacementLimit;
import pl.spkteam.worklifeintegrationserver.task.dto.PlacementLimitDto;
import pl.spkteam.worklifeintegrationserver.task.repo.PlacementLimitRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlacementLimitMapper {

    private final PlacementLimitRepository placementLimitRepository;

    public PlacementLimit mapPlacementLimitDtoToEntity(PlacementLimitDto placementLimitDto) {
        var entity = Optional.ofNullable(placementLimitDto.getId()).flatMap(placementLimitRepository::findById).orElseGet(PlacementLimit::new);
        return entity.setName(placementLimitDto.getName())
                     .setStartTime(placementLimitDto.getStartTime())
                     .setEndTime(placementLimitDto.getEndTime());
    }

    public PlacementLimitDto mapPlacementLimitEntityToDto(PlacementLimit placementLimit) {
        return new PlacementLimitDto(
                placementLimit.getId(),
                placementLimit.getName(),
                placementLimit.getStartTime(),
                placementLimit.getEndTime()
        );
    }
}
