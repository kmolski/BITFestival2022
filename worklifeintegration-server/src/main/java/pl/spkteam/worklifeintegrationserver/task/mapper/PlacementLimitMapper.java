package pl.spkteam.worklifeintegrationserver.task.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.spkteam.worklifeintegrationserver.task.model.PlacementLimit;
import pl.spkteam.worklifeintegrationserver.task.dto.PlacementLimitDto;

@Component
@RequiredArgsConstructor
public class PlacementLimitMapper {

    public PlacementLimit mapPlacementLimitDtoToEntity(PlacementLimitDto placementLimitDto) {
        return new PlacementLimit(
                placementLimitDto.getId(),
                placementLimitDto.getName(),
                placementLimitDto.getStartTime(),
                placementLimitDto.getEndTime()
        );
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
