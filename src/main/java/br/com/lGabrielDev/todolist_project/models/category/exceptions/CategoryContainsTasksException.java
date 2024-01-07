package br.com.lGabrielDev.todolist_project.models.category.exceptions;

public class CategoryContainsTasksException extends RuntimeException{
    
    //constructor
    public CategoryContainsTasksException(String errorMessage){
        super(errorMessage);
    }
}