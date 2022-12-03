package pl.spkteam.worklifeintegrationserver.task.mapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.spkteam.worklifeintegrationserver.task.dto.TaskDto;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.service.PlaceService;
import pl.spkteam.worklifeintegrationserver.task.service.PlacementLimitService;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final PlaceService placeService;
    private final PlacementLimitService placementLimitService;

    public Task mapTaskDtoToEntity(@Valid TaskDto taskDto) {
        return new Task(
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getStartTime(),
                taskDto.getEndTime(),
                placeService.getPlaceById(taskDto.getPlaceId()),
                placementLimitService.getPlacementLimitById(taskDto.getPlacementLimitId()),
                taskDto.getTaskPriority(),
                taskDto.getCategory()
        );
    }

    public TaskDto mapTaskEntityToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getStartTime(),
                task.getEndTime(),
                task.getPlace().getId(),
                task.getPlacementLimit().getId(),
                task.getTaskPriority(),
                task.getCategory()
        );
    }
}
