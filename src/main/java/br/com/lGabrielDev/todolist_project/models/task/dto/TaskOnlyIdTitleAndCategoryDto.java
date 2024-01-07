package br.com.lGabrielDev.todolist_project.models.task.dto;

import java.util.ArrayList;
import java.util.List;
import br.com.lGabrielDev.todolist_project.models.task.Task;

public class TaskOnlyIdTitleAndCategoryDto {
    
    //attributes
    private Long id;
    private String title;
    private Long categoryId;

    //constructors
    public TaskOnlyIdTitleAndCategoryDto(){}

    public TaskOnlyIdTitleAndCategoryDto(Task task){
        this.id = task.getId();
        this.title = task.getTitle();
        this.categoryId = task.getCategory().getId();
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    //toString()
    @Override
    public String toString() {
        return "TaskOnlyIdTitleAndCategoryDto [id=" + id + ", title=" + title + ", categoryId=" + categoryId + "]";
    }

    //transform a list of "tasks" into a list of "TaskOnlyIdTitleAndCategoryDto"
    public static List<TaskOnlyIdTitleAndCategoryDto> convertList(List<Task> tasks){
        List<TaskOnlyIdTitleAndCategoryDto> perfectList = new ArrayList<>();
        for(Task item : tasks){
            TaskOnlyIdTitleAndCategoryDto taskDto = new TaskOnlyIdTitleAndCategoryDto(item);
            perfectList.add(taskDto);
        }
        return perfectList;
    }
}