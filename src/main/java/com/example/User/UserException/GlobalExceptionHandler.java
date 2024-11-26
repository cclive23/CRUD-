package com.example.User.UserException;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        // Create a custom error response object
        ErrorResponse errorResponse = new ErrorResponse(
                "Not Found",  // Error title
                ex.getMessage(),  // Error message
                System.currentTimeMillis() //TimeStamp
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

}
