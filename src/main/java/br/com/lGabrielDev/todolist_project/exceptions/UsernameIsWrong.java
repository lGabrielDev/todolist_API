package br.com.lGabrielDev.todolist_project.exceptions;

public class UsernameIsWrong extends RuntimeException{
    
    //constructor
    public UsernameIsWrong(String errorMessage){
        super(errorMessage);
    }
}