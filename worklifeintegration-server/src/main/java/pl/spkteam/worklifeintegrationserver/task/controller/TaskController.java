package pl.spkteam.worklifeintegrationserver.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.dto.EntityListDto;
import pl.spkteam.worklifeintegrationserver.task.dto.TaskChangelistDto;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;
import pl.spkteam.worklifeintegrationserver.task.service.TaskService;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    private final TaskService taskService;

    @GetMapping
    public EntityListDto<Task> getTasks(@RequestParam(value = "start", required = false) LocalDateTime start,
                                        @RequestParam(value = "end", required = false) LocalDateTime end) {
        return new EntityListDto<>(taskService.getTasksInTimeInterval(start, end));
    }

    @GetMapping("/fromDay")
    public EntityListDto<Task> getTasksFromDay(LocalDateTime date) {
        return new EntityListDto<>(taskService.getTasksFromDay(date));
    }

    @PostMapping
    public TaskChangelistDto createTask(@Valid Task task) {
        var overlappingTasks = taskService.getTasksInTimeInterval(task.getStartTime(), task.getEndTime());
        if (taskService.canTaskBePlaced(overlappingTasks)) {
            return taskService.placeNewTask(task);
        } else {
            return null;
        }
    }


    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
    }

    @PostMapping("/commit")
    public void commitTaskChangelist(@Valid TaskChangelistDto changelist) {
        taskService.saveTaskChangelist(changelist);
    }
}
