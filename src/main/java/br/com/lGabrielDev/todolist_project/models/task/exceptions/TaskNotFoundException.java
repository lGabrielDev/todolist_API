package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class TaskNotFoundException extends RuntimeException{
    
    //constructor
    public TaskNotFoundException(String errorMessage){
        super(errorMessage);
    }
}