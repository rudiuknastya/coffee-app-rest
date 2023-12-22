package project.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName;
            try{
                fieldName = ((FieldError) error).getField();
            } catch (ClassCastException e) {
                if(error.getArguments()[1].toString().equals("id")){
                    fieldName = error.getArguments()[2].toString();
                } else if(error.getArguments()[1].toString().equals("newPassword")){
                    fieldName = error.getArguments()[2].toString();
                } else {
                    fieldName = error.getArguments()[1].toString();
                }
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> notValid(EntityNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> expiredJwt(ExpiredJwtException ex, HttpServletRequest request) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> notValidJwt(MalformedJwtException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);

    }
}
