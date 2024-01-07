package br.com.lGabrielDev.todolist_project.models.task.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import br.com.lGabrielDev.todolist_project.exceptions.DefaultExceptionBody;

@ControllerAdvice
public class TaskGlobalExceptionsHandler {

    //title attribute exceptions
    @ExceptionHandler(TitleAttributeIsWrongException.class)
    public ResponseEntity<DefaultExceptionBody> titleAttributeIsWrongException(TitleAttributeIsWrongException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //description attribute exception
    @ExceptionHandler(DescriptionAttributeIsWrongException.class)
    public ResponseEntity<DefaultExceptionBody> descriptionAttributeIsWrongException(DescriptionAttributeIsWrongException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //priority attribute is wrong exception
    @ExceptionHandler(PriorityAttributeIsWrongException.class)
    public ResponseEntity<DefaultExceptionBody> priorityAttributeIsWrongException(PriorityAttributeIsWrongException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //category attribute is wrong exception
    @ExceptionHandler(CategoryAttributeIsWrongException.class)
    public ResponseEntity<DefaultExceptionBody> categoryAttributeIsWrongException(CategoryAttributeIsWrongException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setErrorMessage(e.getMessage());
        body.setStatus(HttpStatus.CONFLICT.value());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //completed attribute is wrong exception
    @ExceptionHandler(CompletedAttributeIsWrongException.class)
    public ResponseEntity<DefaultExceptionBody> completedAttributeIsWrongException(CompletedAttributeIsWrongException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //task not found exception
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<DefaultExceptionBody> taskNotFoundException(TaskNotFoundException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.NOT_FOUND.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(body);
    }

    //task is from another person exception
    @ExceptionHandler(TaskIsFromAnotherPersonException.class)
    public ResponseEntity<DefaultExceptionBody> taskIsFromAnotherPersonException(TaskIsFromAnotherPersonException e){
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        body.setErrorMessage(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(body);
    }

    //wrong datatype
    //We expect and Integer, but was typed a String
    //We expect and Long, but was typed a String
    //etc...
    @ExceptionHandler(HttpMessageNotReadableException.class)
     public ResponseEntity<DefaultExceptionBody> httpMessageNotReadableException(HttpMessageNotReadableException e){
        //field that occurred the exception
        String failedAttribute = "";
        if(e.getCause().getMessage().contains("[\"title\"])")){
            failedAttribute = "title";
        }
        if(e.getCause().getMessage().contains("[\"description\"])")){
            failedAttribute = "description";
        }
        if(e.getCause().getMessage().contains("[\"priority\"])")){
            failedAttribute = "priority";
        }
        if(e.getCause().getMessage().contains("[\"categoryId\"]")){
            failedAttribute = "categoryId";
        }
        if(e.getCause().getMessage().contains("[\"completed\"])")){
            failedAttribute = "completed";
        }
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());

        if(e.getCause().getMessage().contains("out of range of int")){ //is not an integer
            body.setErrorMessage(String.format("'%s' is wrong. Number too big. We expect an integer between 1 and 3", failedAttribute));
        }
        if(e.getCause().getMessage().contains("Cannot deserialize value of type `java.lang.Integer`")){
            body.setErrorMessage(String.format("'%s' is wrong. We expected an Integer, NOT a String", failedAttribute));
        }
        if(e.getCause().getMessage().contains("out of range of long")){
            body.setErrorMessage(String.format("'%s' is wrong. Number too big for a long", failedAttribute));
        }
        if(e.getCause().getMessage().contains("Cannot deserialize value of type `java.lang.Long` from String")){
            body.setErrorMessage(String.format("'%s' is wrong. We expected a Long, NOT a String", failedAttribute));
        }
        if(e.getCause().getMessage().contains("Cannot deserialize value of type `java.lang.Boolean")){
            body.setErrorMessage(String.format("'%s' is wrong. We expected a Boolean, NOT a String", failedAttribute));
        }
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }

    //request params are wrong
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<DefaultExceptionBody> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        String failedRequestParam = e.getPropertyName();
        String expectedInput = e.getParameter().getParameterType().getSimpleName();
        
        DefaultExceptionBody body = new DefaultExceptionBody();
        body.setTimestamp(LocalDateTime.now());
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setErrorMessage(String.format("@RequestParam '%s' is wrong. We expected a '%s'",failedRequestParam, expectedInput));
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(body);
    }
}