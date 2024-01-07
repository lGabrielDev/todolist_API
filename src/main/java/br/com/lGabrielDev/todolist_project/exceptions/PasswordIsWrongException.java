package br.com.lGabrielDev.todolist_project.exceptions;

public class PasswordIsWrongException extends RuntimeException{
    
    //constructor
    public PasswordIsWrongException(String errorMessage){
        super(errorMessage);
    }
}