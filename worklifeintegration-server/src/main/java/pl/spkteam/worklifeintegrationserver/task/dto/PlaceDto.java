package pl.spkteam.worklifeintegrationserver.task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {

    private Long id;

    @NotNull
    @Size(max = 511)
    private String name;

    @NotNull
    private long transportTimeMinutes;
}
