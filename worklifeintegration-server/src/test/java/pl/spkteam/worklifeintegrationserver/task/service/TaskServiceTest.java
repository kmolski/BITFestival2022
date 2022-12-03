package pl.spkteam.worklifeintegrationserver.task.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import pl.spkteam.worklifeintegrationserver.task.dto.TaskChangelistDto;
import pl.spkteam.worklifeintegrationserver.task.model.*;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {


    private final LocalDateTime beforeDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime middleDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 10, 30, 0);

    private final LocalDateTime sixoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 6, 0, 0);

    private final LocalDateTime fourteenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 14, 0, 0);

    private final LocalDateTime halfaftersixteen = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 30, 0);

    private final LocalDateTime fifteenafterseventeen = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 17, 15, 0);

    private final LocalDateTime halfafternineJuly = LocalDateTime.of(2022,
            Month.JULY, 3, 9, 30, 0);

    private final LocalDateTime fifteenaftersixteenJuly = LocalDateTime.of(2022,
            Month.JULY, 3, 16, 15, 0);

    private final LocalDateTime halfafterseventeen = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime fifteenaftersixteen = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 15, 0);

    private final LocalDateTime elevenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 11, 0, 0);

    private final LocalDateTime thirteenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 13, 0, 0);

    private final LocalDateTime fiveoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 5, 0, 0);

    private final LocalDateTime sevenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 0, 0);

    private final LocalDateTime fifteenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 15, 0, 0);

    private final LocalDateTime sixteenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 0, 0);

    private final LocalDateTime firstConstraint = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 5, 0, 0);

    private final LocalDateTime secondConstraint = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 20, 0, 0);

    private final LocalTime startTimeLimit = LocalTime.of(5, 0, 0,0 );

    private final LocalTime endTimeLimit = LocalTime.of(22, 0, 0,0 );

    private final Duration oneHour = Duration.ofMinutes(60);

    private final Place place = createExamplePlace();

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    //do zmiany
    @Test
    void searchForEmptyPeriodsTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        LocalDateTime expectedStartTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 14, 0, 0);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 15, 0, 0);
        Pair<LocalDateTime, LocalDateTime> expectedEmptyPeriod = Pair.of(expectedStartTime, expectedEndTime);

        Pair<LocalDateTime, LocalDateTime> actualEmptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, createExampleTask6_14());

        Assertions.assertEquals(expectedEmptyPeriod, actualEmptyPeriod);
    }
//do zmiany
    @Test
    void searchForEmptyPeriodsWithMoreExamplesTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask16_1715()));
        LocalDateTime expectedStartTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 17, 15, 0);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 18, 15, 0);
        Pair<LocalDateTime, LocalDateTime> expectedEmptyPeriod = Pair.of(expectedStartTime, expectedEndTime);

        Pair<LocalDateTime, LocalDateTime> actualEmptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, createExampleTask6_14());

        Assertions.assertEquals(expectedEmptyPeriod, actualEmptyPeriod);
    }

    @Test
    void getTasksFromDayTest() {
        Collection<Task> allTasks = new ArrayList<>();
        allTasks.add(createExampleTask6_14());
        allTasks.add(createExampleTask16_1715());
        allTasks.add(createExampleTaskFromAnotherDay());
        Mockito.when(taskRepository.findAll()).thenReturn(allTasks);
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createExampleTask6_14());
        expectedTasks.add(createExampleTask16_1715());

        Collection<Task> actualTasks = taskService.getTasksFromToday(sixoclock);

        Assertions.assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void changeAlreadyExistingTasksAllDayCaseTest() {
        taskService.placeNewTask(createExampleTask6_14());

        TaskChangelistDto actualTasks = taskService.placeNewTask(createExampleLongerThanWorkDayTask());

        Assertions.assertEquals(Collections.emptyList(), actualTasks.splitTasks());
        Assertions.assertEquals(Collections.singletonList(createExampleLongerThanWorkDayTask()), actualTasks.newTasks());
    }

    @Test
    void changeAlreadyExistingTasksAfterWorkCaseTest() {
        taskService.placeNewTask(createExampleTask6_14());

        TaskChangelistDto actualTasks = taskService.placeNewTask(createExampleTask16_1715());

        Assertions.assertEquals(Collections.emptyList(), actualTasks.splitTasks());
        Assertions.assertEquals(Collections.singletonList(createExampleTask16_1715()), actualTasks.newTasks());
    }

    //sprawdzic
    @Test
    void changeAlreadyExistingTasksDuringWorkCaseTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTask6_11());
        expectedTasks.add(createFragmentedTask13_16());
        taskService.placeNewTask(createExampleTask6_14());

        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask6_14()));

        TaskChangelistDto actualTasks = taskService.placeNewTask(createExampleDuringWorkDayTask11_13());

        Assertions.assertEquals(expectedTasks, actualTasks.splitTasks());
        Assertions.assertEquals(Collections.singletonList(createExampleDuringWorkDayTask11_13()), actualTasks.newTasks());
    }

    @Test
    void changeAlreadyExistingTasksBeforeWorkCaseOverlappingTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTask14_15());
        expectedTasks.add(createExampleDuringWorkDayTask5_7());
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask6_14()));
        taskService.placeNewTask(createExampleTask6_14());

        TaskChangelistDto actualTasks = taskService.placeNewTask(createExampleDuringWorkDayTask5_7());

        Assertions.assertEquals(Collections.singletonList(createFragmentedTask7_14()), actualTasks.splitTasks());
        Assertions.assertEquals(expectedTasks, actualTasks.newTasks());
    }

//nie dziala
    @Test
    void changeAlreadyExistingTasksAfterWorkCaseOverlappingTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createExampleTask6_14());
        expectedTasks.add(createFragmentedTask15_16());
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask6_14()));
        taskService.placeNewTask(createExampleTask6_14());

        TaskChangelistDto actualTasks = taskService.placeNewTask(createExampleDuringWorkDayTask13_15());

        Assertions.assertEquals(Collections.singletonList(createFragmentedTask6_13()), actualTasks.splitTasks());
        Assertions.assertEquals(expectedTasks, actualTasks.newTasks());
    }


    @Test
    void sliceTaskFromFrontAndAddToEndOfDay() {
        var newTask = Task.builder()
                .startTime(beforeDateTime)
                .endTime(middleDateTime)
                .taskPriority(Priority.LOW)
                .category(Category.HEALTH_APPOINTMENT)
                .place(place)
                .build();

        Mockito.when(taskRepository.findAll()).thenReturn(List.of(createExampleTask6_14()));
        Assertions.assertEquals(
                new TaskChangelistDto(
                        List.of(createExampleTask6_14().setStartTime(fourteenoclock).setEndTime(fourteenoclock.plusMinutes(150)), newTask),
                        List.of(createExampleTask6_14().setStartTime(middleDateTime).setEndTime(halfaftersixteen))),
                taskService.placeNewTask(newTask)
        );
    }


    private Task createExampleTask6_14() {
        return Task.builder()
                .startTime(sixoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fourteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask16_1715() {
        return Task.builder()
                .startTime(sixteenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fifteenafterseventeen)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTaskFromAnotherDay() {
        return Task.builder()
                .startTime(halfafternineJuly)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fifteenaftersixteenJuly)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleLongerThanWorkDayTask() {
        return Task.builder()
                .startTime(halfafterseventeen)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(fifteenaftersixteen)
                .place(createExamplePlace2())
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleDuringWorkDayTask11_13() {
        return Task.builder()
                .startTime(elevenoclock)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(thirteenoclock)
                .place(createExamplePlace2())
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createFragmentedTask6_11() {
        return Task.builder()
                .startTime(sixoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(elevenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createFragmentedTask13_16() {
        return Task.builder()
                .startTime(thirteenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(sixteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleDuringWorkDayTask5_7() {
        return Task.builder()
                .startTime(fiveoclock)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(sevenoclock)
                .place(createExamplePlace2())
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createFragmentedTask7_14() {
        return Task.builder()
                .startTime(sevenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fourteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createFragmentedTask14_15() {
        return Task.builder()
                .startTime(fourteenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fifteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleDuringWorkDayTask13_15() {
        return Task.builder()
                .startTime(thirteenoclock)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(fifteenoclock)
                .place(createExamplePlace2())
                .placementLimit(createPlacementLimit())
                .build();
    }
    private Task createFragmentedTask6_13() {
        return Task.builder()
                .startTime(sixoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(thirteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }
    private Task createFragmentedTask15_16() {
        return Task.builder()
                .startTime(fifteenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(sixteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }
    private PlacementLimit createPlacementLimit() {
        return PlacementLimit.builder()
                .startTime(startTimeLimit)
                .endTime(endTimeLimit)
                .name("WORKING_LIMITS")
                .build();
    }


    private Place createExamplePlace() {
        Place newPlace = new Place();
        newPlace.setName("GliwiceAEI");
        newPlace.setTransportTimeMinutes(30);
        return newPlace;
    }

    private Place createExamplePlace2() {
        Place newPlace = new Place();
        newPlace.setName("Doctor");
        newPlace.setTransportTimeMinutes(30);
        return newPlace;
    }
}
