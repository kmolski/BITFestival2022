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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {


    private final LocalDateTime beforeDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime startWorkDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 6, 0, 0);

    private final LocalDateTime endWorkDateTime = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 14, 0, 0);

    private final LocalDateTime startWorkDateTime2 = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 30, 0);

    private final LocalDateTime endWorkDateTime2 = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 17, 15, 0);

    private final LocalDateTime startWorkDateTimeFromAnotherDay = LocalDateTime.of(2022,
            Month.JULY, 3, 9, 30, 0);

    private final LocalDateTime endWorkDateTimeFromAnotherDay = LocalDateTime.of(2022,
            Month.JULY, 3, 16, 15, 0);

    private final LocalDateTime startWorkDateTimeLong = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 30, 0);

    private final LocalDateTime endWorkDateTimeLong = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 15, 0);

    private final LocalDateTime startDoctorAppointment = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 11, 0, 0);

    private final LocalDateTime endDoctorAppointment = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 13, 0, 0);

    private final LocalDateTime startDoctorAppointmentA = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 5, 0, 0);

    private final LocalDateTime endDoctorAppointmentA = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 0, 0);

    private final LocalDateTime startWorkDateTimeA = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 7, 0, 0);

    private final LocalDateTime endWorkDateTimeAddedA = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 15, 0, 0);

    private final LocalDateTime endWorkDateTimeAdded = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 16, 0, 0);

    private final LocalDateTime startDoctorAppointmentB = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 13, 0, 0);

    private final LocalDateTime endDoctorAppointmentB = LocalDateTime.of(2022,
            Month.DECEMBER, 3, 15, 0, 0);

    private final Duration oneHour = Duration.ofMinutes(60);

    private final Place place = createExamplePlace();

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Test
    void searchForEmptyPeriodsTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        LocalDateTime expectedStartTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 14, 0, 0);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 15, 0, 0);
        Pair<LocalDateTime, LocalDateTime> expectedEmptyPeriod = Pair.of(expectedStartTime, expectedEndTime);

        Pair<LocalDateTime, LocalDateTime> actualEmptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, createExampleTask());

        Assertions.assertEquals(expectedEmptyPeriod, actualEmptyPeriod);
    }

    @Test
    void searchForEmptyPeriodsWithMoreExamplesTest() {
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask2()));
        LocalDateTime expectedStartTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 17, 15, 0);
        LocalDateTime expectedEndTime = LocalDateTime.of(2022,
                Month.DECEMBER, 3, 18, 15, 0);
        Pair<LocalDateTime, LocalDateTime> expectedEmptyPeriod = Pair.of(expectedStartTime, expectedEndTime);

        Pair<LocalDateTime, LocalDateTime> actualEmptyPeriod = taskService.searchForEmptyPeriods(beforeDateTime, oneHour, createExampleTask());

        Assertions.assertEquals(expectedEmptyPeriod, actualEmptyPeriod);
    }

    @Test
    void getTasksFromDayTest() {
        Collection<Task> allTasks = new ArrayList<>();
        allTasks.add(createExampleTask());
        allTasks.add(createExampleTask2());
        allTasks.add(createExampleTaskFromAnotherDay());
        Mockito.when(taskRepository.findAll()).thenReturn(allTasks);
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createExampleTask());
        expectedTasks.add(createExampleTask2());

        Collection<Task> actualTasks = taskService.getTasksFromDay(startWorkDateTime);

        Assertions.assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void changeAlreadyExistingTasksAllDayCaseTest() {

        Collection<Task> actualTasks = taskService.changeAlreadyExistingTasks(createExampleLongerThanWorkDayTask(), Collections.singleton(createExampleTask()));

        Assertions.assertNull(actualTasks);
    }

    @Test
    void changeAlreadyExistingTasksAfterWorkCaseTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createExampleTask());
        expectedTasks.add(createExampleTask2());

        Collection<Task> actualTasks = taskService.changeAlreadyExistingTasks(createExampleTask2(), Collections.singleton(createExampleTask()));

        Assertions.assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void changeAlreadyExistingTasksDuringWorkCaseTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTask1());
        expectedTasks.add(createFragmentedTask2());
        expectedTasks.add(createExampleDuringWorkDayTask());

        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask()));

        Collection<Task> actualTasks = taskService.changeAlreadyExistingTasks(createExampleDuringWorkDayTask(), Collections.singleton(createExampleTask()));

        Assertions.assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void changeAlreadyExistingTasksBeforeWorkCaseOverlappingTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTaskA1());
        expectedTasks.add(createFragmentedTaskA2());
        expectedTasks.add(createExampleDuringWorkDayTaskA());
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask()));

        Collection<Task> actualTasks = taskService.changeAlreadyExistingTasks(createExampleDuringWorkDayTaskA(), Collections.singleton(createExampleTask()));

        Assertions.assertEquals(expectedTasks, actualTasks);
    }


    @Test
    void changeAlreadyExistingTasksAfterWorkCaseOverlappingTest() {
        Collection<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(createFragmentedTaskB1());
        expectedTasks.add(createFragmentedTaskB2());
        expectedTasks.add(createExampleDuringWorkDayTaskB());
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.singleton(createExampleTask()));

        Collection<Task> actualTasks = taskService.changeAlreadyExistingTasks(createExampleDuringWorkDayTaskB(), Collections.singleton(createExampleTask()));

        Assertions.assertEquals(expectedTasks, actualTasks);
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

    private Task createExampleTask2() {
        return Task.builder()
                .startTime(startWorkDateTime2)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTime2)
                .place(place)
                .build();
    }

    private Task createExampleTaskFromAnotherDay() {
        return Task.builder()
                .startTime(startWorkDateTimeFromAnotherDay)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTimeFromAnotherDay)
                .place(place)
                .build();
    }

    private Task createExampleLongerThanWorkDayTask() {
        return Task.builder()
                .startTime(startWorkDateTimeLong)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(endWorkDateTimeLong)
                .place(createExamplePlace2())
                .build();
    }

    private Task createExampleDuringWorkDayTask() {
        return Task.builder()
                .startTime(startDoctorAppointment)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(endDoctorAppointment)
                .place(createExamplePlace2())
                .build();
    }

    private Task createFragmentedTask1() {
        return Task.builder()
                .startTime(endDoctorAppointmentA)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTime)
                .place(place)
                .build();
    }

    private Task createFragmentedTask2() {
        return Task.builder()
                .startTime(endDoctorAppointment)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTimeAdded)
                .place(place)
                .build();
    }

    private Task createExampleDuringWorkDayTaskA() {
        return Task.builder()
                .startTime(startDoctorAppointmentA)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(endDoctorAppointmentA)
                .place(createExamplePlace2())
                .build();
    }

    private Task createFragmentedTaskA1() {
        return Task.builder()
                .startTime(startWorkDateTimeA)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTime)
                .place(place)
                .build();
    }

    private Task createFragmentedTaskA2() {
        return Task.builder()
                .startTime(endWorkDateTime)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTimeAddedA)
                .place(place)
                .build();
    }

    private Task createExampleDuringWorkDayTaskB() {
        return Task.builder()
                .startTime(startDoctorAppointmentB)
                .taskPriority(Priority.HIGH)
                .category(Category.HEALTH_APPOINTMENT)
                .endTime(endDoctorAppointmentB)
                .place(createExamplePlace2())
                .build();
    }

    private Task createFragmentedTaskB1() {
        return Task.builder()
                .startTime(startWorkDateTime)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endDoctorAppointment)
                .place(place)
                .build();
    }

    private Task createFragmentedTaskB2() {
        return Task.builder()
                .startTime(endDoctorAppointmentB)
                .taskPriority(Priority.LOW)
                .category(Category.OFFICE_WORK)
                .endTime(endWorkDateTimeAdded)
                .place(place)
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