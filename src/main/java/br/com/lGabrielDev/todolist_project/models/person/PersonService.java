package br.com.lGabrielDev.todolist_project.models.person;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import br.com.lGabrielDev.todolist_project.exceptions.PersonAlreadyHasAdminRole;
import br.com.lGabrielDev.todolist_project.exceptions.PersonNotFound;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonCreateDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonFullDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonWithoutTasksDto;
import br.com.lGabrielDev.todolist_project.models.person.validations.CreateAdminUser;
import br.com.lGabrielDev.todolist_project.models.person.validations.PasswordValidations;
import br.com.lGabrielDev.todolist_project.models.person.validations.UsernameValidations;
import br.com.lGabrielDev.todolist_project.models.role.Role;
import br.com.lGabrielDev.todolist_project.models.role.RoleRepository;

@Service
public class PersonService implements UserDetailsService{

    //injected attributes
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordValidations passwordValidations;

    @Autowired
    private UsernameValidations usernameValidations;

    private PersonWithoutTasksDto personWithoutTasksDto;

    @Autowired
    private PasswordEncoder passwordEncoder; //we will use this CLass to 'hash/encode' our person password

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    CreateAdminUser createAdminUser;

    // ----------------------------------- CREATE -----------------------------------
    public PersonFullDto createPerson(PersonCreateDto personDto){
            this.usernameValidations.isUsernameFullyCorrect(personDto.getUsername());
            this.passwordValidations.isPasswordFullyCorrect(personDto.getPassword());
            Person personToBeCreated = new Person(personDto);
            personToBeCreated.setPassword(this.passwordEncoder.encode(personDto.getPassword()));
            //create "ADMIN" role
            this.roleRepository.findByRole(RoleEnum.ADMIN)
                .orElseGet(() -> this.roleRepository.save(new Role(RoleEnum.ADMIN)));
            //create "REGULAR_USER" role
            Role regularUserRole = this.roleRepository.findByRole(RoleEnum.REGULAR_USER)
                .orElseGet(() -> this.roleRepository.save(new Role(RoleEnum.REGULAR_USER)));
            //create an admin user
            this.createAdminUser.createAdminUser();
            //bilaterally
            personToBeCreated.getRoles().add(regularUserRole);
            regularUserRole.getPersons().add(personToBeCreated);
            //save on db
            this.personRepository.save(personToBeCreated); //saved on db
            this.roleRepository.save(regularUserRole); //saved on db
            return new PersonFullDto(personToBeCreated);
    }

    // ------------------------- Give the ADMIN permission -------------------------
    public PersonFullDto giveAdminPermission(Long personId){
        Person personToReceive = this.personRepository.findById(personId)
            .orElseThrow(() -> new PersonNotFound(String.format("person_id #44 not found", personId)));
            
        Role adminRole = this.roleRepository.findByRole(RoleEnum.ADMIN).get();
        //check if the person already has the 'admin_role'
        if(personToReceive.getRoles().contains(adminRole)){
            throw new PersonAlreadyHasAdminRole(String.format("You cannot give the 'ADMIN_ROLE' because person #%d already has it", personId));
        }
        //bilaterally
        personToReceive.getRoles().add(adminRole);
        adminRole.getPersons().add(personToReceive);
        this.personRepository.save(personToReceive); //saved on db
        this.roleRepository.save(adminRole); //saved on db
        return new PersonFullDto(personToReceive);  
    }

    // ----------------------------------- READ -----------------------------------
    public List<PersonWithoutTasksDto> readAllPersons(){
        this.personWithoutTasksDto = new PersonWithoutTasksDto();
        return personWithoutTasksDto.transformList(this.personRepository.findAll());
    }

    // ----------------------------------- Basic Auth - UserDetailsService -----------------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> pOptional = this.personRepository.findByUsername(username);
        if(pOptional.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        Person authenticatedPerson = pOptional.get();
        return authenticatedPerson;
    }
}