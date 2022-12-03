package pl.spkteam.worklifeintegrationserver.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


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
    public Collection<Task> createTask(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Collection<Task> oldTasks = getTasks(task.getStartTime(), task.getEndTime());
        Collection<Task> changedTasksToConfirm = new ArrayList<>();
        if (!oldTasks.isEmpty()) {
            if (!oldTasks.stream().allMatch(Task::isCanBeSplit)) {
                return null;
            }

            for (Task oldTask : oldTasks) {
                LocalDateTime oldStartTime = oldTask.getStartTime();
                LocalDateTime oldEndTime = oldTask.getEndTime();
                Duration duration = Duration.between(startTime, endTime);
                Duration oldDuration = Duration.between(oldStartTime, oldEndTime);
                Duration startingConflict = Duration.between(startTime, oldStartTime);
                Duration endingDuration = Duration.between(startTime, oldEndTime);

                Task newTaskFromOld;
                //gdy przekracza obowiazki dzienne
                if (duration.compareTo(oldDuration) > 0) {
                    oldTask.setEndTime(null);
                    oldTask.setStartTime(null);
                    //oldTask do wykasowania
                    Pair<LocalDateTime, LocalDateTime> newPeriod = searchForEmptyPeriods(oldStartTime, oldDuration, task);
                    newTaskFromOld = createNewTaskBasedOnOlder(oldTask, newPeriod); //dodatek ze starego
                }
                //przypadek gdy na poczatku pracy i konczy sie przed koncem
                else if (startTime.isBefore(oldStartTime) && endTime.isBefore(oldEndTime)) {
                    oldTask.setStartTime(oldStartTime.plus(startingConflict)); //przestawienie starego
                    Pair<LocalDateTime, LocalDateTime> newPeriod = searchForEmptyPeriods(oldStartTime, startingConflict, task);
                    newTaskFromOld = createNewTaskBasedOnOlder(oldTask, newPeriod); //dodatek ze starego
                }
                //gdy jest w srodku
                else {
                    oldTask.setEndTime(startTime); //przestawienie starego
                    Pair<LocalDateTime, LocalDateTime> newPeriod = searchForEmptyPeriods(oldStartTime, endingDuration, task);
                    newTaskFromOld = createNewTaskBasedOnOlder(oldTask, newPeriod); //dodatek ze starego
                }
                changedTasksToConfirm.add(oldTask);
                changedTasksToConfirm.add(newTaskFromOld);
            }
        }
        //po prostu nie ma zadnych wydarzen w przedziale
        changedTasksToConfirm.add(task);
        return changedTasksToConfirm;
    }

    private Task createNewTaskBasedOnOlder(Task oldTask, Pair<LocalDateTime, LocalDateTime> newPeriod) {
        Task newTask = new Task();
        newTask.setStartTime(newPeriod.getFirst());
        newTask.setTaskPriority(oldTask.getTaskPriority());
        newTask.setCategory(oldTask.getCategory());
        newTask.setCanBeSplit(oldTask.isCanBeSplit());
        newTask.setEndTime(newPeriod.getSecond());
        newTask.setPlace(oldTask.getPlace());
        return newTask;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
    }

    //zrobic jak uzytkownik bedzie chcial inna, na razie collection nie robie
    Pair<LocalDateTime, LocalDateTime> searchForEmptyPeriods(LocalDateTime date, Duration duration, Task newTask) {
        Iterable<Task> tasks = getTasksFromDay(date);
        ArrayList<Task> tasksToSort = new ArrayList<>();
        tasks.forEach(tasksToSort::add);
        tasksToSort.add(newTask);

        tasksToSort.sort((Comparator) (o1, o2) -> {
            Task task1 = (Task) o1;
            Task task2 = (Task) o2;
            LocalDateTime x1 = task1.getStartTime();
            LocalDateTime x2 = task2.getStartTime();

            return x1.compareTo(x2);
        }); //przetestowac czy sortuje

        LocalDateTime startTime = tasksToSort.get(tasksToSort.size() - 1).getEndTime();
        LocalDateTime endTime = startTime.plus(duration);
        if (endTime.getDayOfMonth() == startTime.getDayOfMonth())
            return Pair.of(startTime, endTime);
        return null;

        //posprawdzac po end time i do konca dnia czy jest czas - nie konca dnia tylko przedzialu pracy
    }

    @PostMapping
    public void confirmCreatingTasks(Collection<Task> tasks) {
        tasks.forEach(taskRepository::save);
    }


}
