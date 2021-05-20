package tennisclub.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.exceptions.ServiceLayerException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        return new ResponseEntity<>("A database error has occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError error = ex.getFieldError();
        if (error != null) {
            return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("An error has occurred when validating a field.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<String> handleServiceLayerException(ServiceLayerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<String> handleFacadeLayerException(FacadeLayerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
