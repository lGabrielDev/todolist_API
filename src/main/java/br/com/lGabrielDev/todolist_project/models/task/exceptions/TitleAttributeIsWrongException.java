package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class TitleAttributeIsWrongException extends RuntimeException {
    
    //constructor
    public TitleAttributeIsWrongException(String errorMessage){
        super(errorMessage);
    }
}