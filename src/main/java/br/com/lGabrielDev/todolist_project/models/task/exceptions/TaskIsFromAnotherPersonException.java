package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class TaskIsFromAnotherPersonException extends RuntimeException{
    
    //constructor
    public TaskIsFromAnotherPersonException(String errorMessage){
        super(errorMessage);
    }
}