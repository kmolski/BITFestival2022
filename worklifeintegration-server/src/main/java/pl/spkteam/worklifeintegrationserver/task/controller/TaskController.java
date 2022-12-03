package pl.spkteam.worklifeintegrationserver.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "start", required = false) LocalDateTime start,
                               @RequestParam(value = "end", required = false) LocalDateTime end) {
        return taskRepository.findInTimeInterval(start, end);
    }

    @GetMapping("/fromDay")
    public Iterable<Task> getTasksFromDay(LocalDateTime date) {
        Iterable<Task> allTasks = taskRepository.findAll();
        Collection<Task> tasksFromDay = new ArrayList<>();
        for (Task t : allTasks) {
            LocalDateTime currentStartTime = t.getStartTime();
            if (currentStartTime.getYear() == date.getYear() && currentStartTime.getMonthValue() == date.getMonthValue()
                    && currentStartTime.getDayOfMonth() == date.getDayOfMonth()) {
                tasksFromDay.add(t);
            }
        }
        return tasksFromDay;
    }

    @PostMapping
    public Task createTask(Task task) {

        //if jest wydarzenie
        // dodatkowo zmien task poprzedni - jak go znajdziemy -
        Collection<Task> oldTasks = getTasks(); //dodac przedzial
        if (!oldTasks.isEmpty()) {
            // czy nie wykorzystujac z getTasks - start i end wycaigniemy z parametrow task
            // sprawdzic czy task jest przestawialny
            if (checkIfAdjustableTask(oldTask)) {
                // taskRepository.save(oldTask);
                // stworz nowy task na przestawienie pracy
                // taskRepository.save(newTask);
            } else return null;
        }
        //po prostu nie ma zadnych wydarzen w przedziale
        taskRepository.save(task);
        return task;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
    }

    private boolean checkIfAdjustableTask(Task task) {
        return !task.getTaskPriority().equals(Priority.HIGH);
    }
}
