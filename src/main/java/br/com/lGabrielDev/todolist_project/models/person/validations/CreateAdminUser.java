package br.com.lGabrielDev.todolist_project.models.person.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.models.role.Role;
import br.com.lGabrielDev.todolist_project.models.role.RoleRepository;

@Component
public class CreateAdminUser {
    
    //injected attributes
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    public void createAdminUser(){
        Role adminRole = this.roleRepository.findByRole(RoleEnum.ADMIN).get();
        Role regularRole = this.roleRepository.findByRole(RoleEnum.REGULAR_USER).get();

        this.personRepository.findByUsername("admin")
            .orElseGet(() -> {
                Person adminUser = new Person();
                adminUser.setUsername("admin");
                adminUser.setPassword(this.passwordEncoder.encode("123")); //temporary password.
                //bilaterally
                adminUser.getRoles().add(adminRole);
                adminRole.getPersons().add(adminUser);

                adminUser.getRoles().add(regularRole);
                regularRole.getPersons().add(adminUser);
                //save on db
                this.personRepository.save(adminUser);
                this.roleRepository.save(adminRole);
                this.roleRepository.save(regularRole);
                return adminUser;
            });
    }
}