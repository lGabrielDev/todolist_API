package br.com.lGabrielDev.todolist_project.models.category.dtos;

import java.util.ArrayList;
import java.util.List;
import br.com.lGabrielDev.todolist_project.models.category.Category;

public class CategoryWithIdNameAndOwnerIdDto {
    
    //attributes
    private Long id;
    private String name;
    private Long ownerId;
    private Integer taskQuantity;

    //constructors
    public CategoryWithIdNameAndOwnerIdDto(){}

    public CategoryWithIdNameAndOwnerIdDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.ownerId = category.getOwner().getId();
        this.taskQuantity = category.getTasks().size();
    }

    public CategoryWithIdNameAndOwnerIdDto(Long id, String name){
        this.id = id;
        this.name = name;
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Integer getTaskQuantity() {
        return taskQuantity;
    }

    public void setTaskQuantity(Integer taskQuantity) {
        this.taskQuantity = taskQuantity;
    }

    //toString()
    @Override
    public String toString() {
        return "CategoryWithIdNameAndOwnerIdDto [id=" + id + ", name=" + name + ", ownerId=" + ownerId
                + ", taskQuantity=" + taskQuantity + "]";
    }

    //transform a list of "Category" into a list of "CategoryWithIdNameAndOwnerIdDto"
    public static List<CategoryWithIdNameAndOwnerIdDto> transformList(List<Category> categoryListFromDb){
        List<CategoryWithIdNameAndOwnerIdDto> perfectList = new ArrayList<>();

        for(Category item : categoryListFromDb){
            CategoryWithIdNameAndOwnerIdDto personToAdd = new CategoryWithIdNameAndOwnerIdDto(item);
            perfectList.add(personToAdd);
        }
        return perfectList;
    }
    
    //equals()
    @Override
    public boolean equals(Object obj) {
        CategoryWithIdNameAndOwnerIdDto categoryDto = (CategoryWithIdNameAndOwnerIdDto) obj;

        if(
            this.id == categoryDto.getId() &&
            this.name == categoryDto.getName() &&
            this.ownerId == categoryDto.getOwnerId() &&
            this.taskQuantity == categoryDto.getTaskQuantity()
        ){
            return true;
        }
        return false;
    }
}