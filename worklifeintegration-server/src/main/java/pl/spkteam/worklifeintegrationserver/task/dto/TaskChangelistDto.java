package pl.spkteam.worklifeintegrationserver.task.dto;

import jakarta.validation.Valid;
import pl.spkteam.worklifeintegrationserver.task.model.Task;

import java.util.Collection;

public record TaskChangelistDto(Collection<@Valid Task> newTasks,
                                Collection<@Valid Task> splitTasks) {
}
