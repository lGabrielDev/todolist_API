package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class DescriptionAttributeIsWrongException extends RuntimeException{
    
    //constructor
    public DescriptionAttributeIsWrongException(String errorMessage){
        super(errorMessage);
    }
}