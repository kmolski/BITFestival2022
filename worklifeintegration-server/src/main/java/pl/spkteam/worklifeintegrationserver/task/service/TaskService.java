package pl.spkteam.worklifeintegrationserver.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.restapi.exception.BadRequestException;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.model.TaskChangelist;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Collection<Task> getTasksInTimeInterval(LocalDateTime start, LocalDateTime end) {
        return taskRepository.findInTimeInterval(start, end);
    }

    public Pair<LocalDateTime, LocalDateTime> searchForEmptyPeriods(LocalDateTime date, Duration duration, Task newTask) {
        Iterable<Task> tasks = getTasksFromToday(date);
        ArrayList<Task> tasksToSort = new ArrayList<>();
        tasks.forEach(tasksToSort::add);
        tasksToSort.add(newTask);

        tasksToSort.sort((Comparator) (o1, o2) -> {
            Task task1 = (Task) o1;
            Task task2 = (Task) o2;
            LocalDateTime x1 = task1.getStartTime();
            LocalDateTime x2 = task2.getStartTime();

            return x1.compareTo(x2);
        });

        LocalDateTime startTime = tasksToSort.get(tasksToSort.size() - 1).getEndTime();
        LocalDateTime endTime = startTime.plus(duration);
        if (endTime.getDayOfMonth() == startTime.getDayOfMonth())
            return Pair.of(startTime, endTime);
        return null;

        //posprawdzac po end time i do konca dnia czy jest czas - nie konca dnia tylko przedzialu pracy
    }

    public Collection<Task> getTasksFromToday(LocalDateTime date) {
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

    public Task createNewTaskBasedOnOlder(Task oldTask, Pair<LocalDateTime, LocalDateTime> newPeriod) {
        return Task.builder()
                .startTime(newPeriod.getFirst())
                .taskPriority(oldTask.getTaskPriority())
                .category(oldTask.getCategory())
                .endTime(newPeriod.getSecond())
                .place(oldTask.getPlace())
                .build();
    }

    public Collection<Task> changeAlreadyExistingTasks(Task task, Collection<Task> oldTasks) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Collection<Task> changedTasksToConfirm = new ArrayList<>();
        for (Task oldTask : oldTasks) {
            LocalDateTime oldStartTime = oldTask.getStartTime();
            LocalDateTime oldEndTime = oldTask.getEndTime();
            Duration duration = Duration.between(startTime, endTime);
            Duration oldDuration = Duration.between(oldStartTime, oldEndTime);
            Duration startingConflict = Duration.between(startTime, oldStartTime);
            Duration endingDuration = Duration.between(startTime, oldEndTime);

            Task newTaskFromOld;
            //gdy przekracza obowiazki dzienne
            //struktura taski do modyfikacji i usuniecia
            if (duration.compareTo(oldDuration) > 0) {
                oldTask.setEndTime(null);
                oldTask.setStartTime(null);
                //oldTask do wykasowania
                return null;
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
            changedTasksToConfirm.add(task);
        }
        return changedTasksToConfirm;
    }

    @Transactional(readOnly = true)
    public TaskChangelist placeNewTask(Task newTask) {
        var tasksForTheDay = getTasksFromToday(newTask.getStartTime());
        var splits = tasksForTheDay.stream()
                .flatMap(newTask::splitOverlappingTask)
                .collect(Collectors.partitioningBy(newTask::containsTimePeriodOf));
        var splitTasks = splits.get(false);

        var newTasks = splits.get(true);
        tasksForTheDay.add(newTask);
        for (var movedTask : newTasks) {
            movedTask = moveTask(movedTask, tasksForTheDay);
            tasksForTheDay.add(movedTask);
        }
        newTasks.add(newTask);

        var deletedTasks = getTasksInTimeInterval(newTask.getStartTime(), newTask.getEndTime());
        return new TaskChangelist(newTasks, splitTasks, deletedTasks);
    }

    private Task moveTask(Task movedTask, Collection<Task> otherTasks) {
        var duration = Duration.between(movedTask.getStartTime(), movedTask.getEndTime());
        var startTime = otherTasks.stream()
                .map(Task::getEndTime)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalStateException("No other tasks for the given day"));
        var endTime = startTime.plus(duration);
        if (endTime.toLocalTime().isAfter(movedTask.getPlacementLimit().getEndTime())) {
            throw new BadRequestException("Cannot finish moved task today.", List.of(movedTask));
        }
        return movedTask.setStartTime(startTime).setEndTime(endTime);
    }

    @Transactional
    public void saveTaskChangelist(TaskChangelist changelist) {
        taskRepository.saveAll(changelist.newTasks());
        taskRepository.saveAll(changelist.splitTasks());
        taskRepository.deleteAll(changelist.deletedTasks());
    }

    public boolean canTaskBePlaced(Collection<Task> overlappingTasks) {
        return overlappingTasks.stream().allMatch(this::isTaskAdjustable);
    }

    public boolean isTaskAdjustable(Task task) {
        return !task.getTaskPriority().equals(Priority.HIGH);
    }
}
