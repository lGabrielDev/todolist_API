package br.com.lGabrielDev.todolist_project.enums;

public enum RoleEnum {
    
    //CONSTANTS
    REGULAR_USER("REGULAR_USER"),
    ADMIN("ADMIN");

    //attributes
    private String name;

    //constructors
    private RoleEnum(String name){
        this.name = name;
    }
    
    //getters and setters
    public String getName() {
        return name;
    }
}