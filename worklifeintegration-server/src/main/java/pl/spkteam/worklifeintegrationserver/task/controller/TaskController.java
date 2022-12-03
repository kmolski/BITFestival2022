package pl.spkteam.worklifeintegrationserver.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;
import pl.spkteam.worklifeintegrationserver.task.service.TaskService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    private final TaskService taskService;

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "start", required = false) LocalDateTime start,
                               @RequestParam(value = "end", required = false) LocalDateTime end) {
        return taskRepository.findInTimeInterval(start, end);
    }

    @GetMapping("/fromDay")
    public Iterable<Task> getTasksFromDay(LocalDateTime date) {
        return taskService.getTasksFromDay(date);
    }

    @PostMapping
    public Collection<Task> createTask(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Collection<Task> oldTasks = getTasks(task.getStartTime(), task.getEndTime());
        Collection<Task> changedTasksToConfirm = new ArrayList<>();
        if (!oldTasks.isEmpty()) {
            //if (!oldTasks.stream().filter(this::checkIfAdjustableTask).allMatch(true)) {
           //     return null;
           // }
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

    @PostMapping
    public void confirmCreatingTasks(Collection<Task> tasks) {
        tasks.forEach(taskRepository::save);
    }

    private boolean checkIfAdjustableTask(Task task) {
        return !task.getTaskPriority().equals(Priority.HIGH);
    }
}
