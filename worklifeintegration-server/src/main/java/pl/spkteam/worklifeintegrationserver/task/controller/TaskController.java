package pl.spkteam.worklifeintegrationserver.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.restapi.exception.BadRequestException;
import pl.spkteam.worklifeintegrationserver.task.dto.EntityListDto;
import pl.spkteam.worklifeintegrationserver.task.dto.TaskChangelistDto;
import pl.spkteam.worklifeintegrationserver.task.dto.TaskDto;
import pl.spkteam.worklifeintegrationserver.task.mapper.TaskMapper;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;
import pl.spkteam.worklifeintegrationserver.task.service.TaskService;

import java.time.LocalDateTime;

import static java.util.function.Predicate.not;


@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public EntityListDto<TaskDto> getTasks(@RequestParam(value = "start", required = false) LocalDateTime start,
                                           @RequestParam(value = "end", required = false) LocalDateTime end) {
        var dtoList = taskService.getTasksInTimeInterval(start, end).stream().map(taskMapper::mapTaskEntityToDto).toList();
        return new EntityListDto<>(dtoList);
    }

    @GetMapping("/fromDay")
    public EntityListDto<TaskDto> getTasksFromToday(LocalDateTime date) {
        var dtoList = taskService.getTasksFromToday(date).stream().map(taskMapper::mapTaskEntityToDto).toList();
        return new EntityListDto<>(dtoList);
    }

    @PostMapping
    public TaskChangelistDto createTask(@RequestBody @Valid TaskDto taskDto) {
        var task = taskMapper.mapTaskDtoToEntity(taskDto);
        var overlappingTasks = taskService.getTasksInTimeInterval(task.getStartTime(), task.getEndTime());
        if (taskService.canTaskBePlaced(overlappingTasks)) {
            return taskMapper.mapTaskChangelistToDto(taskService.placeNewTask(task));
        } else {
            var message = "Cannot move high priority tasks: " + overlappingTasks.stream()
                    .filter(not(taskService::isTaskAdjustable)).toList();
            throw new BadRequestException(message);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
    }

    @PostMapping("/commit")
    public void commitTaskChangelist(@RequestBody @Valid TaskChangelistDto changelist) {
        taskService.saveTaskChangelist(taskMapper.mapTaskChangelistDtoToModel(changelist));
    }
}
