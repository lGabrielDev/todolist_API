package br.com.lGabrielDev.todolist_project.models.role;

import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority{
    
    //attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role", unique = true)
    @Enumerated(value = EnumType.STRING)
    private RoleEnum role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles") //we informed the B entity column that created the relationship
    Set<Person> persons;

    //constructors
    public Role(){
         this.persons = new HashSet<>(); //initialized for not get a null pointer exception
    }

    public Role(RoleEnum role){
        this.role = role;
        this.persons = new HashSet<>();
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String getAuthority() {
        return this.role.getName();
    }
}