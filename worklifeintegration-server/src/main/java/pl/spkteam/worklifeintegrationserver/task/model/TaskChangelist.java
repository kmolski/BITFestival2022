package pl.spkteam.worklifeintegrationserver.task.model;

import jakarta.validation.Valid;

import java.util.Collection;

public record TaskChangelist(Collection<@Valid Task> newTasks,
                             Collection<@Valid Task> splitTasks,
                             Collection<@Valid Task> deletedTasks) {
}
