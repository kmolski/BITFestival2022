package pl.spkteam.worklifeintegrationserver.task.mapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.spkteam.worklifeintegrationserver.task.dto.TaskDto;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;
import pl.spkteam.worklifeintegrationserver.task.service.PlaceService;
import pl.spkteam.worklifeintegrationserver.task.service.PlacementLimitService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskRepository taskRepository;
    private final PlacementLimitService placementLimitService;
    private final PlaceService placeService;

    public Task mapTaskDtoToEntity(@Valid TaskDto taskDto) {
        var entity = Optional.ofNullable(taskDto.getId()).flatMap(taskRepository::findById).orElseGet(Task::new);
        return entity.setTitle(taskDto.getTitle())
                     .setStartTime(taskDto.getStartTime())
                     .setEndTime(taskDto.getEndTime())
                     .setPlace(placeService.getPlaceById(taskDto.getPlaceId()))
                     .setPlacementLimit(placementLimitService.getPlacementLimitById(taskDto.getPlacementLimitId()))
                     .setTaskPriority(taskDto.getTaskPriority())
                     .setCategory(taskDto.getCategory());
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
