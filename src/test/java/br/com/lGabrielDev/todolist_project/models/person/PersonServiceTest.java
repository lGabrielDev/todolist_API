package br.com.lGabrielDev.todolist_project.models.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import br.com.lGabrielDev.todolist_project.exceptions.PasswordIsWrongException;
import br.com.lGabrielDev.todolist_project.exceptions.PersonAlreadyHasAdminRole;
import br.com.lGabrielDev.todolist_project.exceptions.PersonNotFound;
import br.com.lGabrielDev.todolist_project.exceptions.UsernameIsWrong;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonCreateDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonFullDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonWithoutTasksDto;
import br.com.lGabrielDev.todolist_project.models.person.validations.CreateAdminUser;
import br.com.lGabrielDev.todolist_project.models.person.validations.PasswordValidations;
import br.com.lGabrielDev.todolist_project.models.person.validations.UsernameValidations;
import br.com.lGabrielDev.todolist_project.models.role.Role;
import br.com.lGabrielDev.todolist_project.models.role.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    
    //injected attributes
    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordValidations passwordValidations;

    @Mock
    private UsernameValidations usernameValidations;
    
    @Mock
    private PersonWithoutTasksDto personWithoutTasksDto;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    CreateAdminUser createAdminUser;


    // -------------------- CREATE --------------------
    // --- successfully created ---
    @Test
    @DisplayName("It should create a person successfully")
    public void itShouldCreateAPersonSuccessfully(){
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("user1");
        personDto.setPassword("123");

        Person person = new Person(personDto);

        Role adminRole = new Role();
        adminRole.setRole(RoleEnum.ADMIN);

        Mockito.when(this.usernameValidations.isUsernameFullyCorrect(personDto.getUsername())).thenReturn(true);
        Mockito.when(this.passwordValidations.isPasswordFullyCorrect(personDto.getPassword())).thenReturn(true);
        Mockito.when(this.passwordEncoder.encode(person.getPassword())).thenReturn("123");
        Mockito.when(this.roleRepository.findByRole(Mockito.any(RoleEnum.class))).thenReturn(Optional.of(adminRole));
        Mockito.doNothing().when(this.createAdminUser).createAdminUser();
        Mockito.when(this.personRepository.save(Mockito.any(Person.class))).thenReturn(person);
        Mockito.when(this.roleRepository.save(Mockito.any(Role.class))).thenReturn(adminRole);
        //act
        PersonFullDto methodResult = this.personService.createPerson(personDto);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.getUsername()).isEqualTo("user1");
    }

    // --- not created ---
    @Test
    @DisplayName("It should get an exception because 'username' is not correct")
    public void createItShouldGetAnExceptionBecauseUsernameIsNotCorrect(){
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("abc"); //only 3 characters
        personDto.setPassword("123");

        Mockito.when(this.usernameValidations.isUsernameFullyCorrect(personDto.getUsername())).thenThrow(new UsernameIsWrong("'username' must have between 5 and 20 characters"));
        //act
        Assertions.assertThatThrownBy(() -> this.personService.createPerson(personDto)) 
            //assert
            .isExactlyInstanceOf(UsernameIsWrong.class)
            .hasMessageContaining("'username' must have between 5 and 20 characters");
            Mockito.verify(this.usernameValidations, Mockito.times(1)).isUsernameFullyCorrect(Mockito.anyString());
    }

    @Test
    @DisplayName("It should get an exception because 'password' is not correct")
    public void createItShouldGetAnExceptionBecausePasswordIsNotCorrect(){
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("user1");
        personDto.setPassword("abc"); //there is no numbers here
        
        Mockito.when(this.usernameValidations.isUsernameFullyCorrect(personDto.getUsername())).thenReturn(true);
        Mockito.when(this.passwordValidations.isPasswordFullyCorrect(personDto.getPassword())).thenThrow(new PasswordIsWrongException("'Password' must have at least 1 number"));
        
        //act
        Assertions.assertThatThrownBy(() -> this.personService.createPerson(personDto)) 
            //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("'Password' must have at least 1 number");
            Mockito.verify(this.usernameValidations, Mockito.times(1)).isUsernameFullyCorrect(Mockito.anyString());
            Mockito.verify(this.passwordValidations, Mockito.times(1)).isPasswordFullyCorrect(Mockito.anyString());
    }


    // -------------------- Give the ADMIN permission --------------------
    @Test
    @DisplayName("A person should get the admin permission successfully")
    public void aPersonShouldGetTheAdminPermissionSuccessfully(){
        //arrange
        Person personToReceive = new Person();
        personToReceive.setId(1l);
        personToReceive.setUsername("user1");
        Long personId = personToReceive.getId();
        Mockito.when(this.personRepository.findById(personId)).thenReturn(Optional.of(personToReceive));

        Role adminRole = new Role();
        adminRole.setRole(RoleEnum.ADMIN);
        Mockito.when(this.roleRepository.findByRole(Mockito.any(RoleEnum.class))).thenReturn(Optional.of(adminRole));
        
        Mockito.when(this.personRepository.save(personToReceive)).thenReturn(personToReceive);
        Mockito.when(this.roleRepository.save(adminRole)).thenReturn(adminRole);

        PersonFullDto expectedResult = new PersonFullDto(personToReceive);
        //act
        PersonFullDto methodResult = this.personService.giveAdminPermission(personId);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.getId()).isEqualTo(expectedResult.getId());
        Assertions.assertThat(methodResult.getUsername()).isEqualTo(expectedResult.getUsername());
        Mockito.verify(this.personRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(this.roleRepository, Mockito.times(1)).findByRole(Mockito.any(RoleEnum.class));
        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.roleRepository, Mockito.times(1)).save(Mockito.any(Role.class));
    }

    @Test
    @DisplayName("it should get an exception because person id doesn't exists")
    public void giveAdminPermissionItShouldGetAnExceptionBecausePersonIdDoesNotExists(){
        //arrange
        Long personId = 133l;

        Mockito.when(this.personRepository.findById(personId)).thenReturn(Optional.empty());
        //act
        Assertions.assertThatThrownBy(() -> this.personService.giveAdminPermission(personId))
        //assert
            .isExactlyInstanceOf(PersonNotFound.class)
            .hasMessageContaining(String.format("person_id #44 not found", personId));
        Mockito.verify(this.personRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("it should get an exception because person already has the admin role")
    public void giveAdminPermissionItShouldGetAnExceptionBecausePersonAlreadyHasAdminRole(){
        //arrange
        Person personToReceive = new Person();
        personToReceive.setId(1l);
        personToReceive.setUsername("user1");

        Long personId = personToReceive.getId();

        Mockito.when(this.personRepository.findById(personId)).thenReturn(Optional.of(personToReceive));

        Role adminRole = new Role();
        adminRole.setRole(RoleEnum.ADMIN);

        Mockito.when(this.roleRepository.findByRole(Mockito.any(RoleEnum.class))).thenReturn(Optional.of(adminRole));

        personToReceive.getRoles().add(adminRole);
        //act
        Assertions.assertThatThrownBy(() -> this.personService.giveAdminPermission(personId))
        //assert
            .isExactlyInstanceOf(PersonAlreadyHasAdminRole.class)
            .hasMessageContaining(String.format("You cannot give the 'ADMIN_ROLE' because person #%d already has it", personId));
        Mockito.verify(this.personRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(this.roleRepository, Mockito.times(1)).findByRole(Mockito.any(RoleEnum.class));
    }


    // -------------------- READ --------------------
    @Test
    @DisplayName("It should return a list with 3 persons")
    public void itShouldReturnAListWith3Persons(){
        //arrange
        List<Person> expectedList = List.of(
            new Person("user1", "123"),
            new Person("user2", "123"),
            new Person("user3", "123")
        );

        Mockito.when(this.personRepository.findAll()).thenReturn(expectedList);
        //act
        List<PersonWithoutTasksDto> methodResult = this.personService.readAllPersons();
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).size().isEqualTo(3);
        Assertions.assertThat(methodResult.get(0).getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(1).getUsername()).isEqualTo("user2");
        Assertions.assertThat(methodResult.get(2).getUsername()).isEqualTo("user3");
        Mockito.verify(this.personRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("It should return an empty list of person")
    public void itShouldReturnAnEmptyListOfPerson(){
        //arrange
        List<Person> expectedList = new ArrayList<>();

        Mockito.when(this.personRepository.findAll()).thenReturn(expectedList);
        //act
        List<PersonWithoutTasksDto> methodResult = this.personService.readAllPersons();
        //assert
        Assertions.assertThat(methodResult).isEmpty();
        Mockito.verify(this.personRepository, Mockito.times(1)).findAll();
    }


    // -------------------- Basic Auth - UserDetailsService --------------------
    @Test
    @DisplayName("It should return a person successfully")
    public void basicAuthItShouldReturnAPersonSuccessfully(){
        //arrange
        Person personToBeAuthenticated = new Person("user1", "123");
        String username = personToBeAuthenticated.getUsername();

        Mockito.when(this.personRepository.findByUsername(username)).thenReturn(Optional.of(personToBeAuthenticated));
        //act
        UserDetails methodResult = this.personService.loadUserByUsername(username);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.getUsername()).isEqualTo("user1");
        Mockito.verify(this.personRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }
    
    @Test
    @DisplayName("It should get an exception because it's not an authenticated person")
    public void itShouldGetAnExceptionBecauseItsNotAnAuthenticatedPerson(){
        //arrange
        String username = "usernameThatDoesNotExists";
        Mockito.when(this.personRepository.findByUsername(username)).thenReturn(Optional.empty());
        //act
        Assertions.assertThatThrownBy(() -> this.personService.loadUserByUsername(username))
        //assert
            .isExactlyInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User not found");
        Mockito.verify(this.personRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }
}