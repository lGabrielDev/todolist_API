package br.com.lGabrielDev.todolist_project.models.person;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonCreateDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonWithoutTasksDto;
import br.com.lGabrielDev.todolist_project.models.role.Role;
import br.com.lGabrielDev.todolist_project.models.task.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "person")
public class Person implements UserDetails {
    
    //attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, length = 20)
    private String username;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER) //we informed the B entity column that created the relationship
    private List<Task> tasks;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Category> categories;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
        name = "person_role",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    private Set<Role> roles;

    //constructors
    public Person(){
        this.created_at = LocalDateTime.now();
        this.tasks = new ArrayList<>(); //initialized for not get a null pointer exception
        this.categories = new ArrayList<>(); //initialized for not get a null pointer exception
        this.roles = new HashSet<>(); //initialized for not get a null pointer exception
    }

    public Person(String username, String password){
        this.username = username;
        this.password = password;
        this.created_at = LocalDateTime.now();
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.roles = new HashSet<>();
    }

    public Person(PersonCreateDto person){
        this.username = person.getUsername();
        this.password = person.getPassword();
        this.created_at = LocalDateTime.now();
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.roles = new HashSet<>();
    }

   public Person(PersonWithoutTasksDto person){
        this.username = person.getUsername();
        this.created_at = LocalDateTime.now();
        this.tasks = new ArrayList<>(); 
        this.categories = new ArrayList<>();
        this.roles = new HashSet<>();
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss a").format(this.created_at);
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
  
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public List<Category> getCategories() {
        return categories;
    }


    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    //user details authentication
    @Override
        public String getUsername() {
            return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
        
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    //toString()
    @Override
    public String toString(){
        return
            String.format(
                "#ID: %d\n" +
                "username: %s\n" +
                "password: %s\n" +
                "created_at: %s\n" +
                "tasks_quantity: %d\n", this.id, this.username, this.password, this.created_at.toString(), this.tasks.size()     
            );
     }
}