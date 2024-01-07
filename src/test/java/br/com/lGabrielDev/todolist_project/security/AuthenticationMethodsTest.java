package br.com.lGabrielDev.todolist_project.security;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryContainsTasksException;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryIsFromAnotherPersonException;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNotFoundException;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.Task;
import br.com.lGabrielDev.todolist_project.models.task.TaskRepository;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskIsFromAnotherPersonException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AuthenticationMethodsTest {
    
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CategoryRepository categoryRepository;
   
    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthenticationMethods authenticationMethods;

    private Task task1;
    private Person authenticatedPerson;
    private Category workCategory;


    @BeforeEach
    public void setUp(){
        this.authenticatedPerson = new Person();
        this.authenticatedPerson.setId(1l);
        this.authenticatedPerson.setUsername("user1");

        this.workCategory = new Category();
        this.workCategory.setId(1l);
        this.workCategory.setName("work");
        this.workCategory.setOwner(authenticatedPerson);

        this.task1 = new Task();
        this.task1.setId(1l);
        this.task1.setTitle("task1");
        this.task1.setOwner(authenticatedPerson);
        this.task1.setCategory(workCategory);

    }

    public void personAuthenticatedSetUp(){
        SecurityContextHolder.setContext(this.securityContext);
        when(securityContext.getAuthentication()).thenReturn(this.authentication);
        when(authentication.getPrincipal()).thenReturn(this.authenticatedPerson);
    }


    // ------------------- get the infos from authenticated person  -------------------
    @Test
    @DisplayName("It should return the authenticated Person")
    public void itShouldReturnTheAuthenticatedPerson(){
        //arrange
        this.personAuthenticatedSetUp();
        //act
        Person methodResult = authenticationMethods.getTheAuthenticatedPerson();
        // Assert
        assertNotNull(methodResult);
        assertEquals(1L, methodResult.getId()); 
        assertEquals("user1", methodResult.getUsername());
    }


    // ------------------- Task validations  -------------------
    // --- Task exists ---
    @Test
    @DisplayName("It should NOT throw an exception because task exists")
    public void itShouldNotThrowAnExceptionBecauseTaskExists(){
        // arrange
        Long taskId = this.task1.getId();

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(this.task1));
        // act
        Assertions.assertThatCode(() -> this.authenticationMethods.checkIfTaskExists(taskId))
        // assert
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should throw an exception because task does NOT exists")
    public void itShouldThrowAnExceptionBecauseTaskDoesNotExists(){
        // arrange
        Long taskId = 1l;

        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.empty());
        // act
        Assertions.assertThatThrownBy(() -> this.authenticationMethods.checkIfTaskExists(taskId))
        // assert
            .isExactlyInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining(String.format("Task #%d doesn't exists", taskId));
    }

    // --- A person can only update his own tasks ---
    @Test
    @DisplayName("It should NOT throw an exception because the authenticated person has this task")
    public void itShouldNotThrowAnExceptionBecauseTheAuthenticatedPersonHasThisTask(){
        //arrange
        Task taskToFind = this.task1;
        Long taskId = taskToFind.getId();
        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(taskToFind));

        this.personAuthenticatedSetUp();
        //act
        Assertions.assertThatCode(() -> this.authenticationMethods.checkIfTaskIsFromAnotherPerson(taskId))
        //assert
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should throw an exception because the task is from another person")
    public void itShoulThrowAnExceptionBecauseTheTaskIsFromAnotherPerson(){
        //arrange
        Person taskOwner = new Person();
        taskOwner.setId(2l);
        taskOwner.setUsername("owner");

        Task taskToFind = this.task1;
        taskToFind.setOwner(taskOwner);
        Long taskId = taskToFind.getId();
        Mockito.when(this.taskRepository.findById(taskId)).thenReturn(Optional.of(taskToFind));

        this.personAuthenticatedSetUp();
        //act
        Assertions.assertThatThrownBy(() -> this.authenticationMethods.checkIfTaskIsFromAnotherPerson(taskId))
        //assert
            .isExactlyInstanceOf(TaskIsFromAnotherPersonException.class)
            .hasMessageContaining("You cannot access a task that belongs to another person");
    }


    // ------------------- Category validations  -------------------
    // --- 'Category' exists ---
    @Test
    @DisplayName("It should return a category sucessfully")
    public void itShouldReturnACategorySuccessfully(){
        //arrange
        Long categoryId = this.workCategory.getId();
        Mockito.when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.workCategory));
        //act
        Assertions.assertThatCode(() -> this.authenticationMethods.checkIfCategoryExists(categoryId)) 
        //assert
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should throw an exception because category does not exists")
    public void itShouldThrowAnExceptionBecauseCategoryDoesNotExists(){
        //arrange
        Long categoryId = 1l;
        Mockito.when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        //act
        Assertions.assertThatThrownBy(() -> this.authenticationMethods.checkIfCategoryExists(categoryId)) 
        //assert
            .isExactlyInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining(String.format("Category #%d doesn't exists", categoryId));
    }

    // --- A person can only update his own categories ---
    @Test
    @DisplayName("It should NOT throw an exception because the authenticated person has this category")
    public void itShouldNotThrowAnExceptionBecauseTheAuthenticatedPersonHasThisCategory(){
        //arrange
        this.personAuthenticatedSetUp();

        Category categoryToCheck = this.workCategory;
        Long categoryId = categoryToCheck.getId();

        Mockito.when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryToCheck));
        //act
        Assertions.assertThatCode(() -> this.authenticationMethods.checkIfCategoryIsFromAnotherPerson(categoryId))
        //assert
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should throw an exception because this category is from another person")
    public void itShouldThrowAnExceptionBecauseThisCategoryIsFromAnotherPerson(){
        //arrange
        this.personAuthenticatedSetUp();

        Person anotherOwner = new Person();
        anotherOwner.setId(2l);

        Category categoryToCheck = this.workCategory;
        categoryToCheck.setOwner(anotherOwner);
        Long categoryId = categoryToCheck.getId();

        Mockito.when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryToCheck));
        //act
        Assertions.assertThatThrownBy(() -> this.authenticationMethods.checkIfCategoryIsFromAnotherPerson(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryIsFromAnotherPersonException.class)
            .hasMessageContaining("You cannot access a category that belongs to another person");
    }

    // --- Category contains tasks on it ---
    @Test
    @DisplayName("It should NOT throw an exception because category has no tasks")
    public void itShouldNotThrowAnExceptionBecauseCategoryHasNoTasks(){
        //arrange
        Long categoryId = this.workCategory.getId();

        Mockito.when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.workCategory));
        //act
        Assertions.assertThatCode(() -> this.authenticationMethods.checkIfCategoryContainsTasks(categoryId))
        //assert
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should throw an exception because category has tasks")
    public void itShouldNotThrowAnExceptionBecauseCategoryHasTasks(){
        //arrange
        Long categoryId = this.workCategory.getId();
        
        this.workCategory.getTasks().add(new Task());

        Mockito.when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(this.workCategory));
        //act
        Assertions.assertThatThrownBy(() -> this.authenticationMethods.checkIfCategoryContainsTasks(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryContainsTasksException.class)
            .hasMessageContaining(String.format("You cannot delete category #%d because contains tasks on it. First, you need to delete all tasks from this category", categoryId));
    }
}