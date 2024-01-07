package br.com.lGabrielDev.todolist_project.exceptions;

public class PersonNotFound extends RuntimeException{
    
    //constructor
    public PersonNotFound(String errorMessage){
        super(errorMessage);
    }
}