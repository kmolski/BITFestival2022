package pl.spkteam.worklifeintegrationserver.task.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

public record TaskChangelistDto(@NotNull Collection<@Valid TaskDto> newTasks,
                                @NotNull Collection<@Valid TaskDto> splitTasks,
                                @NotNull Collection<@Valid TaskDto> deletedTasks) {
}
