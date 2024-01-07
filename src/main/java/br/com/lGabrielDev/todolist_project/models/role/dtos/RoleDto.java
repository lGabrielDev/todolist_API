package br.com.lGabrielDev.todolist_project.models.role.dtos;

import java.util.HashSet;
import java.util.Set;
import br.com.lGabrielDev.todolist_project.models.role.Role;

public class RoleDto {

    //attributes
    private Long id;
    private String role;

    //constructors
    public RoleDto(Role role){
        this.id = role.getId();
        this.role = role.getRole().getName();
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //convert a list of "Role" into a list of "RoleDto"
    public static Set<RoleDto> convertTaskList(Set<Role> roleListToConvert){
        Set<RoleDto> perfectList = new HashSet<>();

        for(Role i : roleListToConvert){
            RoleDto convertedTask = new RoleDto(i);
            perfectList.add(convertedTask);
        }
        return perfectList;
    }
}