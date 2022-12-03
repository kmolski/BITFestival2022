package pl.spkteam.worklifeintegrationserver.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.spkteam.worklifeintegrationserver.restapi.exception.BadRequestException;
import pl.spkteam.worklifeintegrationserver.task.model.PlacementLimit;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.model.TaskChangelist;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final PlacementLimitService placementLimitService;

    public Collection<Task> getTasksInTimeInterval(LocalDateTime start, LocalDateTime end) {
        return taskRepository.findInTimeInterval(start, end);
    }

    public Pair<LocalDateTime, LocalDateTime> searchForEmptyPeriods(LocalDateTime todayDate, Duration duration, Task newTask) {
        Iterable<Task> tasks = getTasksFromToday(todayDate);
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
            if (duration.compareTo(oldDuration) > 0) {
                oldTask.setEndTime(null);
                oldTask.setStartTime(null);
                return null;
            } else if (startTime.isBefore(oldStartTime) && endTime.isBefore(oldEndTime)) {
                oldTask.setStartTime(oldStartTime.plus(startingConflict));
                Pair<LocalDateTime, LocalDateTime> newPeriod = searchForEmptyPeriods(oldStartTime, startingConflict, task);
                newTaskFromOld = createNewTaskBasedOnOlder(oldTask, newPeriod);
            } else {
                oldTask.setEndTime(startTime);
                Pair<LocalDateTime, LocalDateTime> newPeriod = searchForEmptyPeriods(oldStartTime, endingDuration, task);
                newTaskFromOld = createNewTaskBasedOnOlder(oldTask, newPeriod);
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

    public TaskChangelist createProposition(Long placementLimitId, Duration length, LocalDateTime date) {
        Collection<Task> tasks = getTasksFromToday(date);
        PlacementLimit placementLimit = placementLimitService.getPlacementLimitById(placementLimitId);
        for (Task currentTask : tasks) {
            Duration duration = Duration.between(currentTask.getStartTime(), currentTask.getEndTime());
            if (isTaskAdjustable(currentTask) && duration.compareTo(length) > 0) {
                if (placementLimit.getStartTime().isBefore(currentTask.getStartTime().toLocalTime()) && placementLimit.getEndTime().isAfter(currentTask.getStartTime().plus(length).toLocalTime())) {
                    Task newTask = Task.builder()
                            .startTime(currentTask.getStartTime())
                            .endTime(currentTask.getStartTime().plus(length))
                            .build();
                    return placeNewTask(newTask);
                }
            }
        }
        return null;
    }

    public Collection<TaskChangelist> getTaskFromAllDays(Long placementLimit, Duration length, Long days, LocalDateTime start) {
        Collection<TaskChangelist> proposedTasks = new ArrayList<>(Collections.emptyList());
        for (int i = 0; i < days; i++) {
            LocalDateTime date = start.plusDays(i);
            proposedTasks.add(createProposition(placementLimit, length, date));
        }
        return proposedTasks;
    }

    public boolean canTaskBePlaced(Collection<Task> overlappingTasks) {
        return overlappingTasks.stream().allMatch(this::isTaskAdjustable);
    }

    public boolean isTaskAdjustable(Task task) {
        return !task.getTaskPriority().equals(Priority.HIGH);
    }


}
