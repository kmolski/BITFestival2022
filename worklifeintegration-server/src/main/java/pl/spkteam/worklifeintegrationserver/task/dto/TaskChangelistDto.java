package pl.spkteam.worklifeintegrationserver.task.dto;

import jakarta.validation.Valid;

import java.util.Collection;

public record TaskChangelistDto(Collection<@Valid TaskDto> newTasks,
                                Collection<@Valid TaskDto> splitTasks,
                                Collection<@Valid TaskDto> deletedTasks) {
}
