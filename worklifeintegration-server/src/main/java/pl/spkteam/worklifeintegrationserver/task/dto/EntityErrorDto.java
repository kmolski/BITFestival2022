package pl.spkteam.worklifeintegrationserver.task.dto;

import java.util.Collection;

public record EntityErrorDto(String message, Collection<?> entities, boolean internal) {
}
