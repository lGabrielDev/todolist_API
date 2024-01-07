package br.com.lGabrielDev.todolist_project.models.task;

import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskCreateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.*;

@Entity
@Table(name = "task")
public class Task {

    //attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "priority")
    private Integer priority; //1 to 3

    @ManyToOne(targetEntity = Person.class)
    @JoinColumn(name = "owner_id")
    private Person owner;

    @ManyToOne(targetEntity = Category.class)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_update")
    private LocalDateTime updatedAt;

    //constructors
    public Task(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(TaskCreateDto taskDto){
        this.title = taskDto.getTitle();
        this.description = taskDto.getDescription();
        this.completed = false;
        this.priority = taskDto.getPriority();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Task(String title, String description, Integer priority, Person owner, Category category){
        this.title = title;
        this.description = description;
        this.completed = false;
        this.priority = priority;
        this.owner = owner;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Category getCategory() {
        return this.category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }


    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    //toString()
    @Override
    public String toString(){
        return
            String.format(
                "#id: %d\n" +
                "title %s\n" +
                "description: %s\n" +
                "completed: %b\n" +
                "priority %d\n" +
                "owner_id: %d\n" +
                "category_id: %d\n" +
                "created_at: %s\n" +
                "last_update: %s\n", this.id, this.title, this.description, this.completed, this.priority, this.owner.getId(), this.category.getId(), this.createdAt.toString(), this.updatedAt.toString()
            );
    }
}