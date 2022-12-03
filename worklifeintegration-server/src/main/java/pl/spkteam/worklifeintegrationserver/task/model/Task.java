package pl.spkteam.worklifeintegrationserver.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.spkteam.worklifeintegrationserver.task.api.TimeIntervalEntity;
import pl.spkteam.worklifeintegrationserver.task.validation.StartTimeBeforeEndTime;

import java.time.LocalDateTime;

@Data
@Entity
@StartTimeBeforeEndTime
public class Task implements TimeIntervalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private PlacementLimit placementLimits;

    @Enumerated(EnumType.ORDINAL)
    private Priority taskPriority;

    @Enumerated(EnumType.ORDINAL)
    private Category category;
}
