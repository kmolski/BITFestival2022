package pl.spkteam.worklifeintegrationserver.task.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import pl.spkteam.worklifeintegrationserver.task.model.Category;
import pl.spkteam.worklifeintegrationserver.task.model.Place;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

class TaskServiceTest {

    private final LocalDateTime beforeDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime startWorkDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 8, 00, 0);

    private final LocalDateTime endWorkDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 00, 0);


    private final Duration oneHour = Duration.ofMinutes(60);

    //private final Task task = createExampleTask();

    private final Place place = createExamplePlace();

    TaskService taskService;

    @Test
    void searchForEmptyPeriodsTest() {
        Task testTask = createExampleTask();
        Pair<LocalDateTime, LocalDateTime> emptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, testTask);
        int xd = 3;
    }

    @Test
    void getTasksFromDayTest() {

    }

    @Test
    void createNewTaskBasedOnOlderTest() {

    }

    @Test
    void changeAlreadyExistingTasksTest() {

    }

    private Task createExampleTask() {
        return Task.builder()
                .startTime(startWorkDateTime)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTime)
                .place(place)
                .build();
    }

    private Place createExamplePlace() {
        Place newPlace = new Place();
        newPlace.setName("GliwiceAEI");
        newPlace.setTransportTimeMinutes(30);
        return newPlace;
    }
}