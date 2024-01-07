package br.com.lGabrielDev.todolist_project.models.task.exceptions;

public class CategoryAttributeIsWrongException extends RuntimeException{
    
    //constructor
    public CategoryAttributeIsWrongException(String errorMessage){
        super(errorMessage);
    }
}