package br.com.lGabrielDev.todolist_project.models.person.validations;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.models.role.Role;
import br.com.lGabrielDev.todolist_project.models.role.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class CreateAdminUserTest {

    //injected attributes
    @InjectMocks
    private CreateAdminUser createAdminUser;

    @Mock
    private PersonRepository personRepository;
    
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Here, we still dont have a 'admin user'. So, it should create an admin successfully")
    public void itShouldCreateAnAdminUserSuccessfully(){
        //arrange
        Role adminRole = new Role();
        adminRole.setRole(RoleEnum.ADMIN);

        Mockito.when(this.roleRepository.findByRole(Mockito.any(RoleEnum.class))).thenReturn(Optional.of(adminRole));
        Mockito.when(this.personRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("123");

        Person adminUser = new Person();
        adminUser.setId(1l);
        adminUser.setUsername("admin");

        Mockito.when(this.personRepository.save(Mockito.any(Person.class))).thenReturn(adminUser);
        Mockito.when(this.roleRepository.save(Mockito.any(Role.class))).thenReturn(adminRole);
        //act
        this.createAdminUser.createAdminUser();
        //assert
        Mockito.verify(this.roleRepository, Mockito.times(2)).findByRole(Mockito.any(RoleEnum.class));
        Mockito.verify(this.personRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(this.passwordEncoder, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.roleRepository, Mockito.times(2)).save(Mockito.any(Role.class));
    }

    @Test
    @DisplayName("Here, we already have a 'admin user'. So, it should not create another one.")
    public void itShouldNotCreateAnAdminUserBecauseWeAlreadyHaveOne(){
        //arrange
        Role adminRole = new Role();
        adminRole.setId(1l);
        adminRole.setRole(RoleEnum.ADMIN);

        Mockito.when(this.roleRepository.findByRole(Mockito.any(RoleEnum.class))).thenReturn(Optional.of(adminRole));

        Person adminUser = new Person();
        adminUser.setId(1l);
        adminUser.setUsername("admin");

        Mockito.when(this.personRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(adminUser));
        //act
        this.createAdminUser.createAdminUser();
        //assert
        Mockito.verify(this.roleRepository, Mockito.times(2)).findByRole(Mockito.any(RoleEnum.class));
        Mockito.verify(this.personRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(this.passwordEncoder, Mockito.times(0)).encode(Mockito.anyString());
        Mockito.verify(this.personRepository, Mockito.times(0)).save(Mockito.any(Person.class));
        Mockito.verify(this.roleRepository, Mockito.times(0)).save(Mockito.any(Role.class));
    }
}