package br.com.lGabrielDev.todolist_project.models.person;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.lGabrielDev.todolist_project.exceptions.PasswordIsWrongException;
import br.com.lGabrielDev.todolist_project.exceptions.PersonNotFound;
import br.com.lGabrielDev.todolist_project.exceptions.UsernameIsWrong;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonCreateDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonFullDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonWithoutTasksDto;
import br.com.lGabrielDev.todolist_project.security.SecurityConfiguration;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class) //we pass the security config class that contains the filter chain method
public class PersonControllerTest {

    //injected attributes
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private PersonService personService;

    // ------------- CREATE -------------
    // --- successfully created ---
    @Test
    @DisplayName("Even when a 'non authenticated user' try to create a person, it should create a 'person' successfully and get a 201 CREATED")
    @WithAnonymousUser
    public void itShouldCreateAPersonSuccessfullyEvenIfTheUserWasNotAuthenticated() throws Exception{
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("user1");
        personDto.setPassword("123");

        PersonFullDto expectedBodyResult = new PersonFullDto();
        expectedBodyResult.setId(1l);
        expectedBodyResult.setUsername(personDto.getUsername());

        Mockito.when(this.personService.createPerson(Mockito.any(PersonCreateDto.class))).thenReturn(expectedBodyResult);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(personDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedBodyResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1l))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(1)).createPerson(Mockito.any(PersonCreateDto.class));
    }

    @Test
    @DisplayName("Even when we are authenticated, it should create a 'person' successfully and get a 201 CREATED")
    @WithMockUser
    public void itShouldCreateAPersonSuccessfullyEvenIfTheUserIsAuthenticated() throws Exception{
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("user1");
        personDto.setPassword("123");

        PersonFullDto expectedBodyResult = new PersonFullDto();
        expectedBodyResult.setId(1l);
        expectedBodyResult.setUsername(personDto.getUsername());

        Mockito.when(this.personService.createPerson(Mockito.any(PersonCreateDto.class))).thenReturn(expectedBodyResult);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(personDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedBodyResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1l))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(1)).createPerson(Mockito.any(PersonCreateDto.class));
    }

    // --- not created ---
    @Test
    @DisplayName("It should get an exception because 'username' is not correct")
    @WithMockUser
    public void createItShouldGetAnExceptionBecauseUsernameIsNotCorrect() throws Exception{
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("user1 someText someText"); //there is 2 whitespaces
        personDto.setPassword("123");

        Mockito.when(this.personService.createPerson(Mockito.any(PersonCreateDto.class)))
            .thenThrow(new UsernameIsWrong("'username' cannot have white spaces"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(personDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("'username' cannot have white spaces"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(1)).createPerson(Mockito.any(PersonCreateDto.class));
    }

    @Test
    @DisplayName("It should get an exception because 'password' is not correct")
    @WithMockUser
    public void createItShouldGetAnExceptionBecausePasswordIsNotCorrect() throws Exception{
        //arrange
        PersonCreateDto personDto = new PersonCreateDto();
        personDto.setUsername("correctUsername");
        personDto.setPassword("123 123 132");  //there is 2 whitespaces

        Mockito.when(this.personService.createPerson(Mockito.any(PersonCreateDto.class)))
            .thenThrow(new PasswordIsWrongException("'Password' cannot have white space"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(personDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("'Password' cannot have white space"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(1)).createPerson(Mockito.any(PersonCreateDto.class));
    }


    // ------------- Give the ADMIN permission -------------
    // --- authenticated person ---
    @Test
    @DisplayName("It should give a permission to a user successfully and return a 200 OK")
    @WithMockUser(authorities = {"ADMIN"})
    public void giveAdminPermissionItShouldGet200OK() throws Exception{
        //arrange
        Long personId = 1l;

        PersonFullDto expectedResult = new PersonFullDto();
        expectedResult.setId(1l);
        expectedResult.setUsername("user1");

        Mockito.when(this.personService.giveAdminPermission(Mockito.anyLong())).thenReturn(expectedResult);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .put("/v1/api/person/give-admin-permission/{id}",  personId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1l))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.atLeast(1)).giveAdminPermission(Mockito.anyLong());
    }

    @Test
    @DisplayName("It should get an exception because the personId doesn't exists")
    @WithMockUser(authorities = {"ADMIN"})
    public void giveAdminPermissionItShouldGetAnExceptionBecausePersonIdDoesntExists() throws Exception{
        //arrange
        Long personId = 1l;

        Mockito.when(this.personService.giveAdminPermission(Mockito.anyLong()))
            .thenThrow(new PersonNotFound("person not found"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .put("/v1/api/person/give-admin-permission/{id}",  personId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("person not found"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.atLeast(1)).giveAdminPermission(Mockito.anyLong());
    }

    // --- NOT an authenticated person ---
    @Test
    @DisplayName("It should get an exception because there is no authenticated person")
    @WithAnonymousUser
    public void giveAdminPermissionItShouldGetAnExceptionBecausePersonIsNotAnAuthenticatedPerson() throws Exception{
        //arrange
        Long personId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .put("/v1/api/person/give-admin-permission/{id}",  personId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(0)).giveAdminPermission(Mockito.anyLong());
    }

    // --- authenticated person, but it does not have the 'ADMIN role' ---
    @Test
    @DisplayName("It should get an exception because the authenticated person doesn't have the 'ADMIN role'")
    @WithMockUser(authorities = {"somethingWeDontWant"})
    public void giveAdminPermissionItShouldGetAnExceptionBecauseTheAuthenticatedPersonDoesntHaveTheAdminRole() throws Exception{
        //arrange
        Long personId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .put("/v1/api/person/give-admin-permission/{id}",  personId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(0)).giveAdminPermission(Mockito.anyLong());
    }


    // ------------- READ -------------
    // --- authenticated person ---
    @Test
    @DisplayName("It should get 200 OK and return a list of 2 'persons'")
    @WithMockUser(authorities = {"ADMIN"})
    public void itShouldGet200OKWithAListOf2Persons() throws Exception{
        //arrange
        PersonWithoutTasksDto user1 = new PersonWithoutTasksDto();
        user1.setId(1l);
        user1.setUsername("user1");

        PersonWithoutTasksDto user2 = new PersonWithoutTasksDto();
        user2.setId(2l);
        user2.setUsername("user2");

        PersonWithoutTasksDto user3 = new PersonWithoutTasksDto();
        user3.setId(3l);
        user3.setUsername("user3");

        List<PersonWithoutTasksDto> expectedList = List.of(user1, user2, user3);
        Mockito.when(this.personService.readAllPersons()).thenReturn(expectedList);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .get("/v1/api/person")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedList)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(user1.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(user1.getUsername()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(user2.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value(user2.getUsername()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(user3.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].username").value(user3.getUsername()))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(1)).readAllPersons();
    }

    @Test
    @DisplayName("It should get 200 OK and return an empty person list")
    @WithMockUser(authorities = {"ADMIN"})
    public void itShouldGet200OKWithAnEmptyPersonList() throws Exception{
        //arrange
        List<PersonWithoutTasksDto> expectedList = new ArrayList<>();
        Mockito.when(this.personService.readAllPersons()).thenReturn(expectedList);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .get("/v1/api/person")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
        
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(1)).readAllPersons();
    }

    // --- NOT an authenticated person ---
    @Test
    @DisplayName("It should get 401 UNAUTHORIZED because there is no authenticated person")
    @WithAnonymousUser
    public void itShouldGet401UnauthorizedBecauseThereIsNoAuthenticatedPerson() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .get("/v1/api/person")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(0)).readAllPersons();
    }

    // --- authenticated person, but it does not have the 'ADMIN role' ---
    @Test
    @DisplayName("It should get 403 FORBIDDEN because the authenticated person does not have the 'ADMIN role'")
    @WithMockUser(authorities = {"somethingWeDontWant"})
    public void itShouldGet403ForbiddenBecauseTheAuthenticatedPersonDoesNotHaveTheAdminRole() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders 
            .get("/v1/api/person")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.personService, Mockito.times(0)).readAllPersons();
    }
}