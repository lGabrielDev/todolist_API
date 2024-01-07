package br.com.lGabrielDev.todolist_project.models.task.dto;

public class TaskUpdateDto {
    
    //attributes
    private String title;
    private String description;
    private Boolean completed;
    private Integer priority; //1 to 3
    private Long categoryId;

    //constructors
    public TaskUpdateDto(){}

    //getters and setters
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    
    
    //toString()
    @Override
    public String toString(){
        return
            String.format(
                "title: %s\n" +
                "description: %s\n" +
                "completed: %b\n" +
                "priority: %d\n" +
                "categoryId: %d\n", this.title, this.description, this.completed, this.priority, this.categoryId
            );
    }
}