package pl.spkteam.worklifeintegrationserver.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "start", required = false) Long start,
                               @RequestParam(value = "end", required = false) Long end) {
        var startTime = Optional.ofNullable(start).map(Instant::ofEpochMilli).orElse(Instant.MIN);
        var endTime = Optional.ofNullable(end).map(Instant::ofEpochMilli).orElse(Instant.MAX);
        return taskRepository.findInTimeInterval(startTime, endTime);
    }

    @GetMapping
    public Iterable<Task> getTasksFromDay(LocalDateTime date) {
        Iterable<Task> allTasks = taskRepository.findAll();
        Iterable<Task> tasksFromDay = new ArrayList<Task>();
        for (Task t : allTasks) {
            currentStartTime = t.getStartTime();
            if (currentStartTime.getYear() == date.getYear() && currentStartTime.getMonthValue() ==
                    date.getMonthValue() && currentStartTime.getDay() == date.getDay()){
                tasksFromDay.add(t);
            }
        }

    }

    @PostMapping
    public void createTask(Task task) {
        taskRepository.save(task);
    }


    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
    }
}
