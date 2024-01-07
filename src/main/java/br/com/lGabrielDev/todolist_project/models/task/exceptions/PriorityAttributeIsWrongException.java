package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class PriorityAttributeIsWrongException extends RuntimeException{

    //constructor
    public PriorityAttributeIsWrongException(String errorMessage){
        super(errorMessage);
    }
}