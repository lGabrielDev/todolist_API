package br.com.lGabrielDev.todolist_project.models.task.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import br.com.lGabrielDev.todolist_project.models.task.Task;

public class TaskFullDto {

    //attributes
    Long id;
    String title;
    String description;
    Boolean completed;
    Integer priority; //1 to 3
    Long ownerId;
    Long categoryId;
    LocalDateTime createdAt;
    LocalDateTime lastUpdate;
 
    //constructors
    public TaskFullDto(){}

    public TaskFullDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.completed = task.getCompleted();
        this.priority = task.getPriority();
        this.ownerId = task.getOwner().getId();
        this.categoryId = task.getCategory().getId();
        this.createdAt = task.getCreatedAt();
        this.lastUpdate = task.getUpdatedAt();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedAt() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss a").format(this.createdAt);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss a").format(this.lastUpdate);
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    //toString()
    @Override
    public String toString() {
        return "TaskFullDto [id=" + id + ", title=" + title + ", description=" + description + ", completed="
                + completed + ", priority=" + priority + ", owner_id=" + ownerId + ", category_id=" + categoryId
                + ", created_at=" + createdAt + ", last_update=" + lastUpdate + "]";
    }

    //convert a list of "Task" into a list of "TasksFullDto"
    public static List<TaskFullDto> convertTaskList(List<Task> taskListToConvert){
        List<TaskFullDto> perfectList = new ArrayList<>();
        for(Task i : taskListToConvert){
            TaskFullDto convertedTask = new TaskFullDto(i);
            perfectList.add(convertedTask);
        }
        return perfectList;
    }
}