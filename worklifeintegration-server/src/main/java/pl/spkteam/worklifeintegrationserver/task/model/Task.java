package pl.spkteam.worklifeintegrationserver.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.spkteam.worklifeintegrationserver.task.api.TimeIntervalObject;
import pl.spkteam.worklifeintegrationserver.task.validation.StartTimeBeforeEndTime;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@StartTimeBeforeEndTime
public class Task implements TimeIntervalObject, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    //jesli polaczenie nie tylko z biura to sie zastanowic
    @NotNull
    @ManyToOne
    private Place place;

    @NotNull
    @ManyToOne
    private PlacementLimit placementLimit;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Priority taskPriority;

    @Enumerated(EnumType.ORDINAL)
    private Category category;

    @Override
    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Stream<Task> splitOverlappingTask(Task task) {
        var intersectionLower = task.startTime.isAfter(startTime) ? task.startTime : startTime;
        var intersectionUpper = task.endTime.isBefore(endTime) ? task.endTime : endTime;
        return Stream.of(task.clone().setEndTime(intersectionLower),
                         task.clone().setStartTime(intersectionLower).setEndTime(intersectionUpper),
                         task.clone().setStartTime(intersectionUpper))
                .filter(splitTask -> splitTask.startTime.isBefore(splitTask.endTime))
                .map(splitTask -> splitTask.setId(null));
    }

    public boolean containsTimePeriodOf(Task task) {
        return (!task.startTime.isBefore(startTime)) && (!task.endTime.isAfter(endTime));
    }
}
