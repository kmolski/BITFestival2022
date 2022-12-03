package pl.spkteam.worklifeintegrationserver.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.dto.EntityListDto;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;
import pl.spkteam.worklifeintegrationserver.task.service.TaskService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;


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
    public Collection<Task> createTask(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Collection<Task> oldTasks = taskService.getTasksInTimeInterval(task.getStartTime(), task.getEndTime());
        Collection<Task> changedTasksToConfirm = new ArrayList<>();
        if (!oldTasks.isEmpty()) {
            for (Task cTask : oldTasks) {
                if (checkIfAdjustableTask(cTask))
                    return null;
            }
            changedTasksToConfirm.addAll(taskService.changeAlreadyExistingTasks(task, startTime, endTime, oldTasks, changedTasksToConfirm));
        }
        //po prostu nie ma zadnych wydarzen w przedziale
        changedTasksToConfirm.add(task);
        return changedTasksToConfirm;
    }


    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
    }

    @PostMapping("/commit")
    public void confirmCreatingTasks(Collection<Task> tasks) {
        tasks.forEach(taskRepository::save);
    }

    private boolean checkIfAdjustableTask(Task task) {
        return !task.getTaskPriority().equals(Priority.HIGH);
    }
}
