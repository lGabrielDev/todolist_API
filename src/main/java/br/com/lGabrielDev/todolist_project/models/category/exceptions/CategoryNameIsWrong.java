package br.com.lGabrielDev.todolist_project.models.category.exceptions;

public class CategoryNameIsWrong extends RuntimeException{
    
    //constructor
    public CategoryNameIsWrong(String errorMessage){
        super(errorMessage);
    }
}