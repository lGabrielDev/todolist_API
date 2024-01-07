package br.com.lGabrielDev.todolist_project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

//customizing exceptions
@ControllerAdvice
public class PersonGlobalExceptionsHandler {

    //username exceptions
    @ExceptionHandler(UsernameIsWrong.class)
    public ResponseEntity<DefaultExceptionBody> usernameIsWrong(UsernameIsWrong e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //password exceptions
    @ExceptionHandler(PasswordIsWrongException  .class)
    public ResponseEntity<DefaultExceptionBody> passwordIsWrongException(PasswordIsWrongException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //person not found
    @ExceptionHandler(PersonNotFound.class)
    public ResponseEntity<DefaultExceptionBody> personNotFound(PersonNotFound e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.NOT_FOUND.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(body);
    }

    //person already has admin role
    @ExceptionHandler(PersonAlreadyHasAdminRole.class)
    public ResponseEntity<DefaultExceptionBody> personAlreadyHasAdminRole(PersonAlreadyHasAdminRole e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.EXPECTATION_FAILED)
            .body(body);
    }
}