package br.com.lGabrielDev.todolist_project.models.task;


import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskCreateDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskFullDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskUpdateDto;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskNotFoundException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TitleAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.security.SecurityConfiguration;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class) //we pass the security config class that contains the filter chain method
public class TaskControllerTest {
    
    //injected attributes
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;
    
    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    private Person ownerOfAll;
    private Category workCategory;
    private Task task1;
    

    @BeforeEach
    public void setUp(){
        this.ownerOfAll = new Person();
        ownerOfAll.setId(1l);
        ownerOfAll.setUsername("user1");

        this.workCategory = new Category();
        this.workCategory.setId(1l);
        this.workCategory.setName("work");

        this.task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.ownerOfAll);
        task1.setCategory(this.workCategory);
    }


    // --------------------------- CREATE ---------------------------
    @Test
    @DisplayName("It should get 401 unauthorized because there is no authenticated person")
    @WithAnonymousUser
    public void createItShouldGet401UnauthorizedBecauseThereIsNoAuthenticatedPerson() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/task")
            .contentType(MediaType.APPLICATION_JSON)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("It should get 403 forbidden because the authenticated person does not have the right authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void itShouldGet403ForbiddenBecausePersonDoesNotHaveTheRightAuthority() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/task")
            .contentType(MediaType.APPLICATION_JSON)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("It should get 201 created and return a list with 1 task on it")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void itShouldGet201AndCreateATaskSucessfully() throws Exception{
        //arrange
        TaskCreateDto taskDto = new TaskCreateDto();
        taskDto.setTitle("task1");
        taskDto.setDescription("some description");

        this.task1.setTitle(taskDto.getTitle());
        this.task1.setDescription(taskDto.getDescription());
        
        TaskFullDto fullDto = new TaskFullDto(this.task1);
        List<TaskFullDto> expectedList = List.of(fullDto);

        Mockito.when(this.taskService.createTask(Mockito.any(TaskCreateDto.class))).thenReturn(expectedList);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(taskDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedList)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("task1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("some description"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).createTask(Mockito.any(TaskCreateDto.class));
    }

    @Test
    @DisplayName("It should get 409 conflict because task 'title' is wrong")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void itShouldGet409ConflictBecauseTaskTitleIsWrong() throws Exception{
        //arrange
        TaskCreateDto taskDto = new TaskCreateDto();
        taskDto.setTitle(null);

        Mockito.when(this.taskService.createTask(Mockito.any(TaskCreateDto.class))).thenThrow(new TitleAttributeIsWrongException("title cannot be null"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/task")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(taskDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("title cannot be null"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).createTask(Mockito.any(TaskCreateDto.class));
    }


    // --------------------------- READ ALL ---------------------------
    @Test
    @DisplayName("It should get 401 unauthorized because there is no authenticated person")
    @WithAnonymousUser
    public void readAllItShouldGet401UnauthorizedBecauseThereIsNoAuthenticatedPerson() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/task")
            .contentType(MediaType.APPLICATION_JSON)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("It should get 403 forbidden because the authenticated person does not have the right authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void readAllItShouldGet403ForbiddenBecausePersonDoesNotHaveTheRightAuthority() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/task")
            .contentType(MediaType.APPLICATION_JSON)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("It should get 200 and return a list with 1 task on it")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void readAllItShouldGet200AndReturnAListWith1TaskOnIt() throws Exception{
        //arrange
        List<TaskFullDto> expectedResult = List.of(new TaskFullDto(this.task1));

        Integer priority = 1;
        Boolean completed = true;
        String titleLike = "something";

        Mockito.when(this.taskService.readAllTasks(anyInt(), anyBoolean(), anyString())).thenReturn(expectedResult);

        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/task")
            .param("priority", this.objMapper.writeValueAsString(priority))
            .param("completed", this.objMapper.writeValueAsString(completed))
            .param("title_like", this.objMapper.writeValueAsString(titleLike))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1l))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("task1"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).readAllTasks(anyInt(), anyBoolean(), anyString());
    }


    // --------------------------- UPDATE ---------------------------
    @Test
    @DisplayName("It should get 401 unauthorized because there is no authenticated person")
    @WithAnonymousUser
    public void updateItShouldGet401UnauthorizedBecauseThereIsNoAuthenticatedPerson() throws Exception{
        //arrange
        Long taskId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .put("/v1/api/task/{id}", taskId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("It should get 403 forbidden because the authenticated person does not have the right authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void updateItShouldGet403ForbiddenBecausePersonDoesNotHaveTheRightAuthority() throws Exception{
        //arrange
        Long taskId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .put("/v1/api/task/{id}", taskId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("It should get 200 and return a list with 1 task on it")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void updateItShouldGet200AndReturnAListWith1TaskOnIt() throws Exception{
        //arrange
        TaskUpdateDto taskDto = new TaskUpdateDto();
        taskDto.setTitle("task1");
        taskDto.setDescription("someDescription");

        this.task1.setTitle(taskDto.getTitle());
        this.task1.setDescription(taskDto.getDescription());

        Long taskId = task1.getId();

        List<TaskFullDto> expectedResult = List.of(new TaskFullDto(this.task1));

        Mockito.when(this.taskService.updateTask(anyLong(), Mockito.any(TaskUpdateDto.class))).thenReturn(expectedResult);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .put("/v1/api/task/{id}", taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(taskDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1l))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("task1"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).updateTask(anyLong(), Mockito.any(TaskUpdateDto.class));
    }

    @Test
    @DisplayName("It should get 409 conflict because title is wrong")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void updateItShouldGet409ConflictBecauseTitleIsWrong() throws Exception{
        //arrange
        TaskUpdateDto taskDto = new TaskUpdateDto();
        taskDto.setTitle(null);
        taskDto.setDescription("someDescription");

        Long taskId = 1l;

        Mockito.when(this.taskService.updateTask(anyLong(), Mockito.any(TaskUpdateDto.class))).thenThrow(new TitleAttributeIsWrongException("'title' cannot be null"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .put("/v1/api/task/{id}", taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(taskDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("'title' cannot be null"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).updateTask(anyLong(), Mockito.any(TaskUpdateDto.class));
    }


    // --------------------------- DELETE ---------------------------
    @Test
    @DisplayName("It should get 401 unauthorized because there is no authenticated person")
    @WithAnonymousUser
    public void deleteItShouldGet401UnauthorizedBecauseThereIsNoAuthenticatedPerson() throws Exception{
        //arrange
        Long taskId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .delete("/v1/api/task/{id}", taskId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("It should get 403 forbidden because the authenticated person does not have the right authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void deleteItShouldGet403ForbiddenBecausePersonDoesNotHaveTheRightAuthority() throws Exception{
        //arrange
        Long taskId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .put("/v1/api/task/{id}", taskId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("It should get 200 and return an empty list")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void deleteShouldGet200AndReturnAnEmptyList() throws Exception{
        //arrange
        Long taskId = 1l;
        List<TaskFullDto> expectedList = new ArrayList<>();

        Mockito.when(this.taskService.deleteTaskById(taskId)).thenReturn(expectedList);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .delete("/v1/api/task/{id}", taskId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedList)))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).deleteTaskById(anyLong());
    }

    @Test
    @DisplayName("It should get 404 not found, because taskId is wrong")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void deleteShouldGet404NotFoundBecauseTaskIdIsWrong() throws Exception{
        //arrange
        Long taskId = 1l;


        Mockito.when(this.taskService.deleteTaskById(taskId)).thenThrow(new TaskNotFoundException("taskId does not exists"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .delete("/v1/api/task/{id}", taskId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("taskId does not exists"))
        .andDo(MockMvcResultHandlers.print());
        Mockito.verify(this.taskService, times(1)).deleteTaskById(anyLong());
    }
}