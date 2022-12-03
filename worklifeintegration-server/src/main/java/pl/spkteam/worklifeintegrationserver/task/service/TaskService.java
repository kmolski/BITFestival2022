package pl.spkteam.worklifeintegrationserver.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Collection<Task> getTasksInTimeInterval(LocalDateTime start, LocalDateTime end) {
        return taskRepository.findInTimeInterval(start, end);
    }

    //zrobic jak uzytkownik bedzie chcial inna, na razie collection nie robie
    public Pair<LocalDateTime, LocalDateTime> searchForEmptyPeriods(LocalDateTime date, Duration duration, Task newTask) {
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
        });

        LocalDateTime startTime = tasksToSort.get(tasksToSort.size() - 1).getEndTime();
        LocalDateTime endTime = startTime.plus(duration);
        if (endTime.getDayOfMonth() == startTime.getDayOfMonth())
            return Pair.of(startTime, endTime);
        return null;

        //posprawdzac po end time i do konca dnia czy jest czas - nie konca dnia tylko przedzialu pracy
    }

    public Collection<Task> getTasksFromDay(LocalDateTime date) {
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

    public Collection<Task> changeAlreadyExistingTasks(Task task, LocalDateTime startTime, LocalDateTime endTime, Collection<Task> oldTasks, Collection<Task> changedTasksToConfirm) {
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
        return changedTasksToConfirm;
    }
}
