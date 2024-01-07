package br.com.lGabrielDev.todolist_project.models.task.dto;

public class TaskCreateDto {
    
    //attributes
    private String title;
    private String description;
    private Integer priority; //1 to 3
    private Long categoryId;

    //constructors
    public TaskCreateDto(){}

    public TaskCreateDto(String title, String description, Integer priority, Long categoryId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.categoryId = categoryId;
    }

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
                "priority: %d\n" +
                "category_id: %d\n", this.title, this.description, this.priority, this.categoryId
            );
     }
}