package br.com.lGabrielDev.todolist_project.models.category.exceptions;

public class CategoryIsFromAnotherPersonException extends RuntimeException {
    
    //constructor
    public CategoryIsFromAnotherPersonException(String errorMessage){
        super(errorMessage);
    }
}