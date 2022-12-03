package pl.spkteam.worklifeintegrationserver.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id, Class<?> clazz) {
        super("Entity %s with ID %d not found!".formatted(clazz.getName(), id));
    }
}
