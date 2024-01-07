package br.com.lGabrielDev.todolist_project.models.category.dtos;

import java.util.List;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskOnlyIdTitleAndCategoryDto;

public class CategoryReadOneDto {
    
    //attributes
    private Long id;
    private String name;
    private Long ownerId;
    private Integer taskQuantity;
    private List<TaskOnlyIdTitleAndCategoryDto> tasks;

    //constructors
    public CategoryReadOneDto(){}

    public CategoryReadOneDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.ownerId = category.getOwner().getId();
        this.taskQuantity = category.getTasks().size();
        this.tasks = TaskOnlyIdTitleAndCategoryDto.convertList(category.getTasks());
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

    public List<TaskOnlyIdTitleAndCategoryDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskOnlyIdTitleAndCategoryDto> tasks) {
        this.tasks = tasks;
    }
}