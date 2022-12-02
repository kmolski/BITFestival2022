package pl.spkteam.worklifeintegrationserver.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    //jesli polaczenie nie tylko z biura to sie zastanowic
    @NotNull
    @ManyToOne // tu dodać jakieś joincolumn czy coś
    private Place place;

    @Enumerated(EnumType.ORDINAL)
    private Priority taskPriority;

    @Enumerated(EnumType.ORDINAL)
    private Category category;

    private boolean canBeSplit;
}