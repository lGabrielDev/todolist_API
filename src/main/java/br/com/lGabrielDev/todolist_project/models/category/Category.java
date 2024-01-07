package br.com.lGabrielDev.todolist_project.models.category;

import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryCreateDto;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.*;

@Entity
@Table(name = "category")
public class Category {

    //attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 20)
    private String name;

    @ManyToOne(targetEntity = Person.class)
    @JoinColumn(name = "owner_id")
    private Person owner;
    
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY) //we informed the B entity column that created the relationship
    private List<Task> tasks;

    //constructors
    public Category(){
        this.tasks = new ArrayList<>(); //initialized to not receive null pointer
    }

    public Category(String name){
        this.tasks = new ArrayList<>(); //initialized to not receive null pointer
    }

    public Category(String name, List<Task> tasks){
        this.name = name;
        this.tasks = tasks;
    }

    public Category(CategoryCreateDto categoryDto){
        this.tasks = new ArrayList<>();
        this.name = categoryDto.getName();
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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    //toString()
    @Override
    public String toString(){
        return
            String.format(
                "#ID: %d\n" +
                "Name: %s\n" +
                "Owner id\n" +
                "task quantity: %d", this.id, this.name, this.owner.getId(), this.tasks.size()
            );
    }
}