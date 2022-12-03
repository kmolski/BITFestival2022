package pl.spkteam.worklifeintegrationserver.task.dto;

import java.util.Collection;

public record EntityListDto<T>(Collection<T> items) {
}
