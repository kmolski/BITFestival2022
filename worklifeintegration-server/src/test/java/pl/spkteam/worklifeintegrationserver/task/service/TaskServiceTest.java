package pl.spkteam.worklifeintegrationserver.task.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import pl.spkteam.worklifeintegrationserver.task.model.Category;
import pl.spkteam.worklifeintegrationserver.task.model.Place;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.model.Task;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {


    private final LocalDateTime beforeDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime startWorkDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 8, 0, 0);

    private final LocalDateTime endWorkDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 0, 0);


    private final Duration oneHour = Duration.ofMinutes(60);

    private final Task testTask = createExampleTask();

    private final Place place = createExamplePlace();

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Test
    void searchForEmptyPeriodsTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        LocalDateTime expectedStartTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 16, 0, 0);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 17, 0, 0);
        Pair<LocalDateTime, LocalDateTime> expectedEmptyPeriod = Pair.of(expectedStartTime, expectedEndTime);

        Pair<LocalDateTime, LocalDateTime> actualEmptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, testTask);

        Assertions.assertEquals(expectedEmptyPeriod, actualEmptyPeriod);

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