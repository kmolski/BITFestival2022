package pl.spkteam.worklifeintegrationserver.task.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import pl.spkteam.worklifeintegrationserver.task.model.*;
import pl.spkteam.worklifeintegrationserver.task.repo.TaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private final LocalDateTime beforeDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime sixoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 6, 0, 0);

    private final LocalDateTime fourteenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 14, 0, 0);

    private final LocalDateTime eightoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 4, 8, 0, 0);

    private final LocalDateTime tenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 4, 10, 0, 0);

    private final LocalDateTime halfafternine = LocalDateTime.of(2022,
            Month.DECEMBER, 4, 9, 30, 0);

    private final LocalDateTime halfaftereleven = LocalDateTime.of(2022,
            Month.DECEMBER, 4, 11, 30, 0);

    private final LocalDateTime halfafterfifteen = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 15, 30, 0);

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

    private final LocalDateTime eighteenoclock = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 18, 0, 0);

    private final LocalDateTime halfafterseven = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalTime startTimeLimit = LocalTime.of(5, 0, 0, 0);

    private final LocalTime endTimeLimit = LocalTime.of(22, 0, 0, 0);

    private final Duration oneHour = Duration.ofMinutes(60);

    private final Place place = createExamplePlace();

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    PlacementLimitService placementLimitService;

    @Test
    void searchForEmptyPeriodsTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(List.of());
        LocalDateTime expectedStartTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 14, 0, 0);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 15, 0, 0);
        Pair<LocalDateTime, LocalDateTime> expectedEmptyPeriod = Pair.of(expectedStartTime, expectedEndTime);

        Pair<LocalDateTime, LocalDateTime> actualEmptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, createExampleTask6_14());

        Assertions.assertEquals(expectedEmptyPeriod, actualEmptyPeriod);
    }

    @Test
    void searchForEmptyPeriodsWithMoreExamplesTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(List.of(createExampleTask16_1715()));
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
        List<Task> allTasks = new ArrayList<>();
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
        TaskChangelist actualTasks = taskService.placeNewTask(createExampleLongerThanWorkDayTask());

        Assertions.assertEquals(List.of(), actualTasks.splitTasks());
        Assertions.assertEquals(List.of(createExampleLongerThanWorkDayTask()), actualTasks.newTasks());
    }

    @Test
    void changeAlreadyExistingTasksAfterWorkCaseTest() {
        TaskChangelist actualTasks = taskService.placeNewTask(createExampleTask16_1715());

        Assertions.assertEquals(List.of(), actualTasks.splitTasks());
        Assertions.assertEquals(List.of(createExampleTask16_1715()), actualTasks.newTasks());
    }

    @Test
    void changeAlreadyExistingTasksDuringWorkCaseTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTask6_11());
        expectedTasks.add(createFragmentedTask13_14());

        Mockito.when(taskRepository.findAll()).thenReturn(List.of(createExampleTask6_14()));
        TaskChangelist actualTasks = taskService.placeNewTask(createExampleDuringWorkDayTask11_13());

        Assertions.assertEquals(expectedTasks, actualTasks.splitTasks());
        Assertions.assertEquals(List.of(createMovedTask14_16(), createExampleDuringWorkDayTask11_13()), actualTasks.newTasks());
    }

    @Test
    void changeAlreadyExistingTasksBeforeWorkCaseOverlappingTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTask14_15());
        expectedTasks.add(createExampleDuringWorkDayTask5_7());

        Mockito.when(taskRepository.findAll()).thenReturn(List.of(createExampleTask6_14()));
        TaskChangelist actualTasks = taskService.placeNewTask(createExampleDuringWorkDayTask5_7());

        Assertions.assertEquals(List.of(createFragmentedTask7_14()), actualTasks.splitTasks());
        Assertions.assertEquals(expectedTasks, actualTasks.newTasks());
    }

    @Test
    void changeAlreadyExistingTasksAfterWorkCaseOverlappingTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(List.of(createExampleTask6_14()));
        TaskChangelist actualTasks = taskService.placeNewTask(createExampleDuringWorkDayTask13_15());

        Assertions.assertEquals(List.of(createFragmentedTask6_13()), actualTasks.splitTasks());
        Assertions.assertEquals(List.of(createFragmentedTask15_16(), createExampleDuringWorkDayTask13_15()), actualTasks.newTasks());
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

    private Task createExampleTask8_10() {
        return Task.builder()
                .startTime(eightoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(tenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask16_1715() {
        return Task.builder()
                .startTime(eighteenoclock)
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

    private Task createFragmentedTask13_14() {
        return Task.builder()
                .startTime(thirteenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fourteenoclock)
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

    private Task createMovedTask14_16() {
        return Task.builder()
                .startTime(fourteenoclock)
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

    private Task createExampleTask6_730() {
        return Task.builder()
                .startTime(sixoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(halfafterseven)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask14_1530() {
        return Task.builder()
                .startTime(fourteenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(halfafterfifteen)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask730_14() {
        return Task.builder()
                .startTime(halfafterseven)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(fourteenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask8_930() {
        return Task.builder()
                .startTime(eightoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(halfafternine)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask930_10() {
        return Task.builder()
                .startTime(halfafternine)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(tenoclock)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    private Task createExampleTask10_1130() {
        return Task.builder()
                .startTime(tenoclock)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(halfaftereleven)
                .place(place)
                .placementLimit(createPlacementLimit())
                .build();
    }

    @Test
    void createPropositionTest() {
        Long id = 1L;
        Mockito.when(placementLimitService.getPlacementLimitById(id)).thenReturn(createPlacementLimit());
        Duration duration = Duration.between(LocalTime.NOON, LocalTime.of(13, 30)); //czyli 1,5h
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(createExampleTask6_14());
        Mockito.when(taskRepository.findAll()).thenReturn(allTasks);

        TaskChangelist taskChangelist = taskService.createProposition(id, duration, beforeDateTime);

        Assertions.assertEquals(Stream.of(createExampleTask730_14()).count(), taskChangelist.splitTasks().size());
        Assertions.assertEquals(createExampleTask730_14().getStartTime(), taskChangelist.splitTasks().iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask730_14().getEndTime(), taskChangelist.splitTasks().iterator().next().getEndTime());

        Assertions.assertEquals(Stream.of(createExampleTask14_1530(), createExampleTask6_730()).count(), taskChangelist.newTasks().size());
        Assertions.assertEquals(createExampleTask14_1530().getStartTime(), taskChangelist.newTasks().iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask14_1530().getEndTime(), taskChangelist.newTasks().iterator().next().getEndTime());
        Assertions.assertEquals(createExampleTask6_730().getStartTime(), taskChangelist.newTasks().stream().skip(1).iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask6_730().getEndTime(), taskChangelist.newTasks().stream().skip(1).iterator().next().getEndTime());

    }

    @Test
    void getTaskFromAllDaysTest() {
        Long id = 1L;
        Mockito.when(placementLimitService.getPlacementLimitById(id)).thenReturn(createPlacementLimit());
        Duration duration = Duration.between(LocalTime.NOON, LocalTime.of(13, 30));
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(createExampleTask6_14());
        allTasks.add(createExampleTask8_10());
        Mockito.when(taskRepository.findAll()).thenReturn(allTasks);
        Long days = 2L;

        Collection<TaskChangelist> testChangeLists = taskService.getTaskFromAllDays(id, duration, days, beforeDateTime);

        TaskChangelist firstTaskChangeList = testChangeLists.iterator().next();
        Assertions.assertEquals(Stream.of(createExampleTask730_14()).count(), firstTaskChangeList.splitTasks().size());
        Assertions.assertEquals(createExampleTask730_14().getStartTime(), firstTaskChangeList.splitTasks().iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask730_14().getEndTime(), firstTaskChangeList.splitTasks().iterator().next().getEndTime());

        Assertions.assertEquals(Stream.of(createExampleTask14_1530(), createExampleTask6_730()).count(), firstTaskChangeList.newTasks().size());
        Assertions.assertEquals(createExampleTask14_1530().getStartTime(), firstTaskChangeList.newTasks().iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask14_1530().getEndTime(), firstTaskChangeList.newTasks().iterator().next().getEndTime());
        Assertions.assertEquals(createExampleTask6_730().getStartTime(), firstTaskChangeList.newTasks().stream().skip(1).iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask6_730().getEndTime(), firstTaskChangeList.newTasks().stream().skip(1).iterator().next().getEndTime());

        TaskChangelist secondTaskChangeList = testChangeLists.stream().skip(1).iterator().next();
        Assertions.assertEquals(Stream.of(createExampleTask930_10()).count(), secondTaskChangeList.splitTasks().size());
        Assertions.assertEquals(createExampleTask930_10().getStartTime(), secondTaskChangeList.splitTasks().iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask930_10().getEndTime(), secondTaskChangeList.splitTasks().iterator().next().getEndTime());

        Assertions.assertEquals(Stream.of(createExampleTask8_930(), createExampleTask10_1130()).count(), secondTaskChangeList.newTasks().size());
        Assertions.assertEquals(createExampleTask10_1130().getStartTime(), secondTaskChangeList.newTasks().iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask10_1130().getEndTime(), secondTaskChangeList.newTasks().iterator().next().getEndTime());
        Assertions.assertEquals(createExampleTask8_930().getStartTime(), secondTaskChangeList.newTasks().stream().skip(1).iterator().next().getStartTime());
        Assertions.assertEquals(createExampleTask8_930().getEndTime(), secondTaskChangeList.newTasks().stream().skip(1).iterator().next().getEndTime());


    }
}
