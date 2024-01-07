package br.com.lGabrielDev.todolist_project.models.task;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskCreateDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskFullDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskUpdateDto;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.CategoryAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.DescriptionAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.PriorityAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskIsFromAnotherPersonException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskNotFoundException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TitleAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.models.task.validations.CategoryIdValidations;
import br.com.lGabrielDev.todolist_project.models.task.validations.DescriptionValidations;
import br.com.lGabrielDev.todolist_project.models.task.validations.PriorityValidations;
import br.com.lGabrielDev.todolist_project.models.task.validations.TitleValidations;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    
    //injected attributes
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private TitleValidations titleValidations;
    @Mock
    private DescriptionValidations descriptionValidations;
    @Mock
    private PriorityValidations priorityValidations;
    @Mock
    private CategoryIdValidations categoryIdValidations;
    @Mock
    private AuthenticationMethods authenticationMethods;
    @Mock
    private CategoryRepository categoryRepository;

    private Person authenticatedPerson;
    private Category taskCategory;
    private TaskCreateDto taskCreateDto;
    private TaskUpdateDto updateDto;

    @BeforeEach
    public void setUp(){
        this.authenticatedPerson = new Person();
        this.authenticatedPerson.setId(1l);
        this.authenticatedPerson.setUsername("user1");

        this.taskCategory = new Category();
        this.taskCategory.setId(1l);
        this.taskCategory.setName("work");
        this.taskCategory.setOwner(this.authenticatedPerson);

        this.taskCreateDto = new TaskCreateDto();
        taskCreateDto.setTitle("someTitle");
        taskCreateDto.setDescription("someDescription");
        taskCreateDto.setPriority(1);
        taskCreateDto.setCategoryId(1l);

        this.updateDto = new TaskUpdateDto();
        this.updateDto.setTitle("some new title");
        this.updateDto.setDescription("some description");
        this.updateDto.setPriority(1);
        this.updateDto.setCategoryId(33l);
    }

    // ------------------------- CREATE -------------------------
    // --- created successfully ---
    @Test
    @DisplayName("It should create a task successfully")
    public void itShouldCreateATaskSuccessfully(){
        //arrange
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(taskCreateDto.getTitle());
        Mockito.when(this.descriptionValidations.descriptionHasThePerfectLength(taskCreateDto.getDescription())).thenReturn(true);
        Mockito.when(this.priorityValidations.isPriorityValid(taskCreateDto.getPriority())).thenReturn(true);
        Mockito.doNothing().when(this.categoryIdValidations).categoryIdIsFullyCorrect(taskCreateDto.getCategoryId());
        
        Task taskToBeCreated = new Task(taskCreateDto);
        taskToBeCreated.setId(1l);
        taskToBeCreated.setOwner(this.authenticatedPerson);
        taskToBeCreated.setCategory(this.taskCategory);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.categoryRepository.findById(taskCreateDto.getCategoryId())).thenReturn(Optional.of(this.taskCategory));
        
        Mockito.when(this.taskRepository.save(Mockito.any())).thenReturn(taskToBeCreated);
        Mockito.when(this.personRepository.save(Mockito.any())).thenReturn(this.authenticatedPerson);
        Mockito.when(this.categoryRepository.save(taskCategory)).thenReturn(this.taskCategory);

        List<Task> expectedList = List.of(taskToBeCreated);
        Mockito.when(this.taskRepository.findAll(authenticatedPerson.getId())).thenReturn(expectedList);
        //act
        List<TaskFullDto> methodResult = this.taskService.createTask(this.taskCreateDto);
        //assert
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("someTitle");
        Assertions.assertThat(methodResult.get(0).getDescription()).isEqualTo("someDescription");
        Mockito.verify(this.titleValidations, times(1)).titleIsFullyCorrect(Mockito.anyString());
        Mockito.verify(this.descriptionValidations, times(1)).descriptionHasThePerfectLength(Mockito.anyString());
        Mockito.verify(this.priorityValidations, times(1)).isPriorityValid(anyInt());
        Mockito.verify(this.categoryIdValidations, times(1)).categoryIdIsFullyCorrect(anyLong());
        Mockito.verify(this.authenticationMethods, times(1)).getTheAuthenticatedPerson();
        Mockito.verify(this.categoryRepository, times(1)).findById(anyLong());
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong());   
    }

    // --- not created ---
    @Test
    @DisplayName("It should get an exception because title is not correct")
    public void itShouldGetAnExceptionBecauseTitleIsNotCorrect(){
        //arrange
        Mockito.doThrow(new TitleAttributeIsWrongException("title is null"))
            .when(this.titleValidations).titleIsFullyCorrect(this.taskCreateDto.getTitle());
        //act
        Assertions.assertThatThrownBy(() -> this.taskService.createTask(this.taskCreateDto))
        //assert
            .isExactlyInstanceOf(TitleAttributeIsWrongException.class);
        Mockito.verify(this.titleValidations, times(1)).titleIsFullyCorrect(anyString());
    }

    @Test
    @DisplayName("It should get an exception because description is not correct")
    public void itShouldGetAnExceptionBecauseDescriptionIsNotCorrect(){
        //arrange
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(this.taskCreateDto.getTitle());

        Mockito.doThrow(new DescriptionAttributeIsWrongException("description is null"))
            .when(this.descriptionValidations).descriptionHasThePerfectLength(this.taskCreateDto.getDescription());
        //act
        Assertions.assertThatThrownBy(() -> this.taskService.createTask(this.taskCreateDto))
        //assert
            .isExactlyInstanceOf(DescriptionAttributeIsWrongException.class);
        Mockito.verify(this.titleValidations, times(1)).titleIsFullyCorrect(anyString());
        Mockito.verify(this.descriptionValidations, times(1)).descriptionHasThePerfectLength(anyString());
    }

    @Test
    @DisplayName("It should get an exception because priority is not correct")
    public void itShouldGetAnExceptionBecausePriorityIsNotCorrect(){
        //arrange
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(this.taskCreateDto.getTitle());
        Mockito.when(this.descriptionValidations.descriptionHasThePerfectLength(this.taskCreateDto.getDescription())).thenReturn(true);
        
        Mockito.when(this.priorityValidations.isPriorityValid(this.taskCreateDto.getPriority()))
            .thenThrow(new PriorityAttributeIsWrongException("priority is null"));
        //act
        Assertions.assertThatThrownBy(() -> this.taskService.createTask(this.taskCreateDto))
        //assert
            .isExactlyInstanceOf(PriorityAttributeIsWrongException.class)
            .hasMessageContaining("priority is null");
        Mockito.verify(this.titleValidations, times(1)).titleIsFullyCorrect(anyString());
        Mockito.verify(this.descriptionValidations, times(1)).descriptionHasThePerfectLength(anyString());
        Mockito.verify(this.priorityValidations, times(1)).isPriorityValid(anyInt());
    }


    // ------------------------- READ ALL -------------------------
    // --- without filters ---
    @Test
    @DisplayName("It should return a list of 2 Tasks, without any filter, successfully")
    public void itShouldReturnAListOf2TasksWithOutAnyFilterSuccessfully(){
        //arrange
        Integer priority = null;
        Boolean completed = null;
        String titleLike = null;

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);

        Task task2 = new Task();
        task2.setId(2l);
        task2.setTitle("task2");
        task2.setOwner(this.authenticatedPerson);
        task2.setCategory(this.taskCategory);

        List<Task> expectedTaskList = List.of(task1, task2);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId())).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).hasSize(2);
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(1).getId()).isEqualTo(2l);
        Assertions.assertThat(methodResult.get(1).getTitle()).isEqualTo("task2");
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong());
    }

    // --- with filters ---
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' only")
    public void itShouldReturnAListOf1TaskFilteredByPriorityOnly(){
        //arrange
        Integer priority = 2;
        Boolean completed = null;
        String titleLike = null;

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);
        task1.setPriority(2);

        List<Task> expectedTaskList = List.of(task1);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId(), priority)).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedTaskList.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(2);
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong(), anyInt());
    }
    
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'completed' only")
    public void itShouldReturnAListOf1TaskFilteredByCompletedOnly(){
        //arrange
        Integer priority = null;
        Boolean completed = true;
        String titleLike = null;

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);
        task1.setCompleted(true);

        List<Task> expectedTaskList = List.of(task1);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId(), completed)).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedTaskList.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getCompleted()).isEqualTo(true);
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong(), anyBoolean());
    }

    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'titleLike' only")
    public void itShouldReturnAListOf1TaskFilteredByTitleLikeOnly(){
        //arrange
        Integer priority = null;
        Boolean completed = null;
        String titleLike = "someTitle";

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);

        List<Task> expectedTaskList = List.of(task1);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId(), titleLike)).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedTaskList.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong(), anyString());
    }

    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' AND 'completed'")
    public void itShouldReturnAListOf1TaskFilteredByPriorityAndCompleted(){
        //arrange
        Integer priority = 1;
        Boolean completed = true;
        String titleLike = null;

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);
        task1.setPriority(priority);
        task1.setCompleted(completed);

        List<Task> expectedTaskList = List.of(task1);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId(), priority, completed)).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedTaskList.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(1);
        Assertions.assertThat(methodResult.get(0).getCompleted()).isEqualTo(true);
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong(), anyInt(), anyBoolean());
    }

    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' AND 'titleLike'")
    public void itShouldReturnAListOf1TaskFilteredByPriorityAndTitleLike(){
        //arrange
        Integer priority = 1;
        Boolean completed = null;
        String titleLike = "someTitle";

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);
        task1.setPriority(priority);

        List<Task> expectedTaskList = List.of(task1);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId(), priority, titleLike)).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedTaskList.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(1);
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong(), anyInt(), anyString());
    }

    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' AND 'completed' AND 'titleLike'")
    public void itShouldReturnAListOf1TaskFilteredByPriorityAndCompletedAndTitleLike(){
        //arrange
        Integer priority = 1;
        Boolean completed = true;
        String titleLike = "someTitle";

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);
        task1.setPriority(priority);
        task1.setCompleted(completed);

        List<Task> expectedTaskList = List.of(task1);

        Mockito.when(this.taskRepository.findAll(this.authenticatedPerson.getId(), priority, completed, titleLike)).thenReturn(expectedTaskList);
        //act
        List<TaskFullDto> methodResult = this.taskService.readAllTasks(priority, completed, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedTaskList.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(1);
        Assertions.assertThat(methodResult.get(0).getCompleted()).isEqualTo(true);
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong(), anyInt(), anyBoolean(), anyString());
    }


    // ------------------------- UPDATE -------------------------
    // --- updated successfully ---
    @Test
    @DisplayName("It should update a task successfully, because all dto attributes are correct")
    public void itShouldUpdateATaskSuccessfullyBecauseAllDtoAttributesAreCorrect(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);
        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        
        Task updatedTask = new Task();
        updatedTask.setId(1l);
        updatedTask.setCategory(this.taskCategory);
        updatedTask.setOwner(this.authenticatedPerson);

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(updatedTask));
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        //validations
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(this.updateDto.getTitle());
        Mockito.when(this.descriptionValidations.descriptionHasThePerfectLength(this.updateDto.getDescription())).thenReturn(true);
        Mockito.when(this.priorityValidations.isPriorityValid(this.updateDto.getPriority())).thenReturn(true);
        Mockito.doNothing().when(this.categoryIdValidations).categoryIdIsFullyCorrect(this.updateDto.getCategoryId());
        Mockito.when(this.categoryRepository.findById(this.updateDto.getCategoryId())).thenReturn(Optional.of(this.taskCategory));
        Mockito.when(this.categoryRepository.save(Mockito.any(Category.class))).thenReturn(this.taskCategory);
        Mockito.when(this.taskRepository.save(Mockito.any(Task.class))).thenReturn(updatedTask);

        List<Task> expectedListResult = List.of(updatedTask);
        Mockito.when(this.taskRepository.findAll(authenticatedPerson.getId())).thenReturn(expectedListResult);
        //act
        List<TaskFullDto> methodResult = this.taskService.updateTask(taskId, this.updateDto);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(expectedListResult.size());
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("some new title");
        Assertions.assertThat(methodResult.get(0).getDescription()).isEqualTo("some description");
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(anyLong());
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskIsFromAnotherPerson(anyLong());
        Mockito.verify(this.taskRepository, times(1)).findById(anyLong());
        Mockito.verify(this.authenticationMethods, times(1)).getTheAuthenticatedPerson();
        Mockito.verify(this.titleValidations, times(1)).titleIsFullyCorrect(anyString());
        Mockito.verify(this.descriptionValidations, times(1)).descriptionHasThePerfectLength(anyString());
        Mockito.verify(this.priorityValidations, times(1)).isPriorityValid(anyInt());
        Mockito.verify(this.categoryIdValidations, times(1)).categoryIdIsFullyCorrect(anyLong());
        Mockito.verify(this.categoryRepository, times(1)).findById(anyLong());
        Mockito.verify(this.categoryRepository, times(1)).save(Mockito.any(Category.class));
        Mockito.verify(this.taskRepository, times(1)).save(Mockito.any(Task.class));
        Mockito.verify(this.taskRepository, times(1)).findAll(anyLong());
    }

    // --- general exceptions ---
    @Test
    @DisplayName("It should get an exception because task does not exists")
    public void itShouldGetAnExceptionBecauseTaskDoesNotExists(){
        //arrange
        Long taskId = 1l;

        Mockito.doThrow(new TaskNotFoundException("task not found")).when(this.authenticationMethods).checkIfTaskExists(taskId);
        //act
        Assertions.assertThatThrownBy(() -> this.taskService.updateTask(taskId, this.updateDto))
        //assert
            .isExactlyInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("task not found");
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(anyLong()); 
    }

    @Test
    @DisplayName("It should get an exception because task is from another person")
    public void itShouldGetAnExceptionBecauseTaskIfFromAnotherPerson(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);

        Mockito.doThrow(new TaskIsFromAnotherPersonException("task belongs to another person")).when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        //act
        Assertions.assertThatThrownBy(() -> this.taskService.updateTask(taskId, this.updateDto))
        //assert
            .isExactlyInstanceOf(TaskIsFromAnotherPersonException.class)
            .hasMessageContaining("task belongs to another person");
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(anyLong()); 
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskIsFromAnotherPerson(anyLong()); 
    }

    // --- 'title' exception ---
    @Test
    @DisplayName("It should get an exception because title is wrong")
    public void updateItShouldGetAnExceptionBecauseTitleIsWrong(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);
        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        
        Task updatedTask = new Task();
        updatedTask.setId(1l);
        updatedTask.setCategory(this.taskCategory);
        updatedTask.setOwner(this.authenticatedPerson);

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(updatedTask));
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        //validations
        Mockito.doThrow(new TitleAttributeIsWrongException("title is null"))
            .when(this.titleValidations).titleIsFullyCorrect(this.updateDto.getTitle());
        //act + assert
        Assertions.assertThatCode(() -> this.taskService.updateTask(taskId, this.updateDto))
            .isExactlyInstanceOf(TitleAttributeIsWrongException.class)
            .hasMessageContaining("title is null");
    }

    // --- 'description' exception ---
    @Test
    @DisplayName("It should get an exception because description is wrong")
    public void updateItShouldGetAnExceptionBecauseDescriptionIsWrong(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);
        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        
        Task updatedTask = new Task();
        updatedTask.setId(1l);
        updatedTask.setCategory(this.taskCategory);
        updatedTask.setOwner(this.authenticatedPerson);

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(updatedTask));
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        //validations
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(this.updateDto.getTitle());
        
        Mockito.doThrow(new DescriptionAttributeIsWrongException("description is wrong"))
            .when(this.descriptionValidations).descriptionHasThePerfectLength(this.updateDto.getDescription());
        //act + assert
        Assertions.assertThatCode(() -> this.taskService.updateTask(taskId, this.updateDto))
            .isExactlyInstanceOf(DescriptionAttributeIsWrongException.class)
            .hasMessageContaining("description is wrong");
    }

    // --- 'priority' exception ---
    @Test
    @DisplayName("It should get an exception because priority is wrong")
    public void updateItShouldGetAnExceptionBecausePriorityIsWrong(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);
        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        
        Task updatedTask = new Task();
        updatedTask.setId(1l);
        updatedTask.setCategory(this.taskCategory);
        updatedTask.setOwner(this.authenticatedPerson);

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(updatedTask));
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        //validations
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(this.updateDto.getTitle());
        Mockito.when(this.descriptionValidations.descriptionHasThePerfectLength(this.updateDto.getDescription())).thenReturn(true);
        
        Mockito.doThrow(new PriorityAttributeIsWrongException("'priority' must be 1, 2 or 3"))
            .when(this.priorityValidations).isPriorityValid(this.updateDto.getPriority());
        //act + assert
        Assertions.assertThatCode(() -> this.taskService.updateTask(taskId, this.updateDto))
            .isExactlyInstanceOf(PriorityAttributeIsWrongException.class)
            .hasMessageContaining("'priority' must be 1, 2 or 3");
    }
    
    // --- 'category_id' exception ---
    @Test
    @DisplayName("It should get an exception because category_id is wrong")
    public void updateItShouldGetAnExceptionBecauseCategoryIdIsWrong(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);
        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        
        Task updatedTask = new Task();
        updatedTask.setId(1l);
        updatedTask.setCategory(this.taskCategory);
        updatedTask.setOwner(this.authenticatedPerson);

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(updatedTask));
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);

        //validations
        Mockito.doNothing().when(this.titleValidations).titleIsFullyCorrect(this.updateDto.getTitle());
        Mockito.when(this.descriptionValidations.descriptionHasThePerfectLength(this.updateDto.getDescription())).thenReturn(true);
        Mockito.when(this.priorityValidations.isPriorityValid(this.updateDto.getPriority())).thenReturn(true);
        
        Mockito.doThrow(new CategoryAttributeIsWrongException("categoryId is null"))
            .when(this.categoryIdValidations).categoryIdIsFullyCorrect(this.updateDto.getCategoryId());
        //act + assert
        Assertions.assertThatCode(() -> this.taskService.updateTask(taskId, this.updateDto))
            .isExactlyInstanceOf(CategoryAttributeIsWrongException.class)
            .hasMessageContaining("categoryId is null");
    } 


    // ------------------------- DELETE -------------------------
    // --- deleted successfully ---
    @Test
    @DisplayName("It should delete a task successfully and return a list containing 1 task")
    public void itShouldDeleteATaskSuccessfullyAndReturnAListContainingOnly1Task(){
        //arrange
        Long taskId = 1l;
        Long ownerId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);
        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        Mockito.doNothing().when(this.taskRepository).deleteById(taskId);

        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setDescription("some description");
        task1.setOwner(this.authenticatedPerson);
        task1.setCategory(this.taskCategory);

        List<Task> expectedListResult = List.of(task1);

        Mockito.when(this.taskRepository.findAll(ownerId)).thenReturn(expectedListResult);
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
         
        //act
        List<TaskFullDto> methodResult = this.taskService.deleteTaskById(taskId);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getDescription()).isEqualTo("some description");
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(taskId);
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskIsFromAnotherPerson(taskId);
        Mockito.verify(this.taskRepository, times(1)).deleteById(taskId);
    }

    // --- not deleted ---
    @Test
    @DisplayName("You cannot delete a task that does not exists. So, it will get an exception")
    public void deleteItShouldGetAnExceptionBecauseTaskDoesNotExists(){
        //arrange
        Long taskId = 1l;

        Mockito.doThrow(new TaskNotFoundException("task does not exists"))
            .when(this.authenticationMethods).checkIfTaskExists(taskId);

        //act
        Assertions.assertThatThrownBy(() -> this.taskService.deleteTaskById(taskId))
        //assert
            .isExactlyInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("task does not exists");
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(taskId);   
    }

    @Test
    @DisplayName("You cannot delete a task from another person. So, it will get an exception")
    public void deleteItShouldGetAnExceptionBecauseTaskBelongsToAnotherPerson(){
        //arrange
        Long taskId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfTaskExists(taskId);

        Mockito.doThrow(new TaskIsFromAnotherPersonException("you cannot delete tasks from another person"))
            .when(this.authenticationMethods).checkIfTaskIsFromAnotherPerson(taskId);
        //act
        Assertions.assertThatThrownBy(() -> this.taskService.deleteTaskById(taskId))
        //assert
            .isExactlyInstanceOf(TaskIsFromAnotherPersonException.class)
            .hasMessageContaining("you cannot delete tasks from another person");
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(taskId);
        Mockito.verify(this.authenticationMethods, times(1)).checkIfTaskExists(taskId);
    }
}