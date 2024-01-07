package br.com.lGabrielDev.todolist_project.models.person.dtos;

import java.util.List;
import java.util.Set;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.role.dtos.RoleDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskFullDto;

public class PersonFullDto {
    
    //attributes
    private Long id;
    private String username;
    private List<TaskFullDto> tasks;
    private Set<RoleDto> roles;
    private String created_at;

    //constructors
    public PersonFullDto(){}

    public PersonFullDto(Person person){
        this.id = person.getId();
        this.username = person.getUsername();
        this.tasks = TaskFullDto.convertTaskList(person.getTasks());
        this.roles = RoleDto.convertTaskList(person.getRoles());
        this.created_at = person.getCreated_at();
    }
    
    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<TaskFullDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskFullDto> tasks) {
        this.tasks = tasks;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    //toString()
    @Override
    public String toString() {
        return "PersonFullDto [id=" + id + ", username=" + username + ", tasks=" + tasks + ", roles=" + roles
                + ", created_at=" + created_at + "]";
    } 
}