package br.com.lGabrielDev.todolist_project.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//every time an exception occurs, we will put this object on the RequestBody
public class DefaultExceptionBody {

    //attributes
    private LocalDateTime timestamp;
    private Integer status;
    private String errorMessage;

    //constructors
    public DefaultExceptionBody(){} 

    public DefaultExceptionBody(LocalDateTime timestamp, Integer status, String errorMessage){
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.errorMessage = errorMessage;
    }

    //getters and setters
    public String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss a").format(this.timestamp);
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}