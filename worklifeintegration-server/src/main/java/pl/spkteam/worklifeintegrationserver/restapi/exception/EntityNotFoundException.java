package pl.spkteam.worklifeintegrationserver.restapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    private final Class<?> clazz;

    public EntityNotFoundException(Long id, Class<?> clazz) {
        super("Entity %s with ID %d not found.".formatted(clazz.getName(), id));
        this.clazz = clazz;
    }
}
