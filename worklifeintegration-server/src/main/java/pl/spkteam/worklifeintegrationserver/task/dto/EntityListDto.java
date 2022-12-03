package pl.spkteam.worklifeintegrationserver.task.dto;

public record EntityListDto<T>(Iterable<T> items) {
}
