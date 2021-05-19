package tennisclub.controllers;

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
    ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError error = ex.getFieldError();
        if (error != null) {
            return ResponseEntity.badRequest().body(error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body("An error has occurred when validating a field.");
    }

    @ExceptionHandler
    ResponseEntity<String> handleServiceLayerException(ServiceLayerException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler
    ResponseEntity<String> handleFacadeLayerException(FacadeLayerException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
