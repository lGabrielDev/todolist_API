package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class CompletedAttributeIsWrongException extends RuntimeException{

    //constructor
    public CompletedAttributeIsWrongException(String ErrorMessage){
        super(ErrorMessage);
    }
}