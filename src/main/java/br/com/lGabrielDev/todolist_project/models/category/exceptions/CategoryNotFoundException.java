package br.com.lGabrielDev.todolist_project.models.category.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    
    //constructor
    public CategoryNotFoundException(String errorMessage){
        super(errorMessage);
    }
}