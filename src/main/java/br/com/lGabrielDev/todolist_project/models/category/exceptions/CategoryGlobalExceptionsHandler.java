package br.com.lGabrielDev.todolist_project.models.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import br.com.lGabrielDev.todolist_project.exceptions.DefaultExceptionBody;

@ControllerAdvice
public class CategoryGlobalExceptionsHandler {
    
    //category name is Wrong
    @ExceptionHandler(CategoryNameIsWrong.class)
    public ResponseEntity<DefaultExceptionBody> categoryNameIsWrong(CategoryNameIsWrong e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //category doesn't exists
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<DefaultExceptionBody> categoryNotFoundException(CategoryNotFoundException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.NOT_FOUND.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(body);
    }

    //category is from another person exception
    @ExceptionHandler(CategoryIsFromAnotherPersonException.class)
    public ResponseEntity<DefaultExceptionBody> categoryIsFromAnotherPersonException(CategoryIsFromAnotherPersonException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(body);
    }

    //category contains tasks exception
    @ExceptionHandler(CategoryContainsTasksException.class)
    public ResponseEntity<DefaultExceptionBody> categoryContainsTasksException(CategoryContainsTasksException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.EXPECTATION_FAILED)
            .body(body);
    }
}