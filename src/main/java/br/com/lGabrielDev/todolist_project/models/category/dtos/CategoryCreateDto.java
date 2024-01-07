package br.com.lGabrielDev.todolist_project.models.category.dtos;

public class CategoryCreateDto {
    
    //attributes
    private String name;

    //constructors
    public CategoryCreateDto(){}

    public CategoryCreateDto(String name){
        this.name = name;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //toString()
    @Override
    public String toString(){
        return String.format("category name: %s", this.name);
    }
}