package br.com.lGabrielDev.todolist_project.models.person.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.role.dtos.RoleDto;

public class PersonWithoutTasksDto {

    //attributes
    private Long id;
    private String username;
    private String created_at;
    private Set<RoleDto> roles;

    //constructors
    public PersonWithoutTasksDto(){}

    public PersonWithoutTasksDto(Person person){
        this.id = person.getId();
        this.username = person.getUsername();
        this.created_at = person.getCreated_at();
        this.roles = RoleDto.convertTaskList(person.getRoles());
        
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }
    
    //toString()
    @Override
    public String toString(){
        return
            String.format(
                "#ID: %d\n" +
                "username: %s\n" +
                "created_at: %s\n", this.id, this.username, this.created_at
            );
    }

    //transform a list of "person" into a list of "PersonWithoutTasksDto"
    public List<PersonWithoutTasksDto> transformList(List<Person> personListFromDb){
        List<PersonWithoutTasksDto> perfectList = new ArrayList<>();
        for(Person item : personListFromDb){
            PersonWithoutTasksDto personToAdd = new PersonWithoutTasksDto(item);
            perfectList.add(personToAdd);
        }
        return perfectList;
    }
}