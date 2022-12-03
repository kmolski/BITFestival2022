package pl.spkteam.worklifeintegrationserver.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.spkteam.worklifeintegrationserver.task.model.Category;
import pl.spkteam.worklifeintegrationserver.task.model.Priority;
import pl.spkteam.worklifeintegrationserver.task.validation.StartTimeBeforeEndTime;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@StartTimeBeforeEndTime
public class TaskDto {

    private Long id;

    @NotNull
    @Size(max = 511)
    private String title;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime endTime;

    @NotNull
    private Long placeId;

    @NotNull
    private Long placementLimitId;

    @NotNull
    private Priority taskPriority;

    private Category category;
}
