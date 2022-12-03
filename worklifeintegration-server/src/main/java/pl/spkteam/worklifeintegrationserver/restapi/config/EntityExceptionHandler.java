package pl.spkteam.worklifeintegrationserver.restapi.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.spkteam.worklifeintegrationserver.restapi.exception.BadRequestException;
import pl.spkteam.worklifeintegrationserver.restapi.exception.EntityNotFoundException;
import pl.spkteam.worklifeintegrationserver.task.dto.EntityErrorDto;

import java.util.List;

@RestControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<EntityErrorDto> handleBadRequest(BadRequestException ex, WebRequest request) {
        var errorDto = new EntityErrorDto(ex.getMessage(), ex.getEntities(), false);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityErrorDto> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        var errorDto = new EntityErrorDto(ex.getMessage(), List.of(), false);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EntityErrorDto> handleConstraintViolation(MethodArgumentNotValidException ex, WebRequest request) {
        var errorDto = new EntityErrorDto(ex.getDetailMessageCode(), ex.getAllErrors(), false);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<EntityErrorDto> handleConstraintViolation(JsonMappingException ex, WebRequest request) {
        var errorDto = new EntityErrorDto(ex.getMessage(), List.of(ex.getPath()), true);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
