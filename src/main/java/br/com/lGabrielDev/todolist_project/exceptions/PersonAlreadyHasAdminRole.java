package br.com.lGabrielDev.todolist_project.exceptions;

public class PersonAlreadyHasAdminRole extends RuntimeException{
    
    //constructor
    public PersonAlreadyHasAdminRole(String errorMessage){
        super(errorMessage);
    }
}