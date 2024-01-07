package br.com.lGabrielDev.todolist_project.models.task.validations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.Task;
import br.com.lGabrielDev.todolist_project.models.task.TaskRepository;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TitleAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TitleValidationsTest {
    
    @InjectMocks
    private TitleValidations titleValidations;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    AuthenticationMethods authenticationMethods;

    private Person authenticatedPerson;

    @BeforeEach
    public void setUp(){
        this.authenticatedPerson = new Person();
        this.authenticatedPerson.setId(1l);
        this.authenticatedPerson.setUsername("user1");
    }


    // ------------- title cannot be null -------------
    @Test
    @DisplayName("It should throw an exception because title is NULL")
    public void itShouldThrowAnExceptionBecauseTitleIsNull(){
        //arrange
        String titleToCheck = null;
        //act + assert
        Assertions.assertThatThrownBy(() -> this.titleValidations.titleIsNull(titleToCheck))
            .isExactlyInstanceOf(TitleAttributeIsWrongException.class)
            .hasMessageContaining("'title' cannot be null");
    }

    @Test
    @DisplayName("It should return FALSE because title is NOT NULL")
    public void itShouldReturnFalseBecauseTitleIsNotNull(){
        //arrange
        String titleToCheck = "something";
        //act + assert
        Assertions.assertThatCode(() -> this.titleValidations.titleIsNull(titleToCheck))
            .doesNotThrowAnyException();
    }


    // ------------- title length between 1 and 50 -------------
    @Test
    @DisplayName("It should throw an exception because title length contains less than 1 character")
    public void itShouldThrowAnExceptionBecauseTitleLengthContainsLessThan1Character(){
        //arrange
        String titleToCheck = "";
        //act
        Assertions.assertThatThrownBy(() -> this.titleValidations.titleLengthHasThePerfectSize(titleToCheck))
        //assert
            .isExactlyInstanceOf(TitleAttributeIsWrongException.class)
            .hasMessageContaining("'title' length must have between 1 and 50 characters");
    }

    @Test
    @DisplayName("It should throw an exception because title length contains more than 50 character")
    public void itShouldThrowAnExceptionBecauseTitleLengthContainsMoreThan50Characters(){
        //arrange
        String titleToCheck = "something something something something something something something something";
        //act
        Assertions.assertThatThrownBy(() -> this.titleValidations.titleLengthHasThePerfectSize(titleToCheck))
        //assert
            .isExactlyInstanceOf(TitleAttributeIsWrongException.class)
            .hasMessageContaining("'title' length must have between 1 and 50 characters");
    }

    @Test
    @DisplayName("It should return true because title has the perfect length")
    public void itShouldReturnTrueBecaseTitleHasThePerfectLength(){
        //arrange
        String titleToCheck = "perfectTitle";
        //act
        Assertions.assertThatCode(() -> this.titleValidations.titleLengthHasThePerfectSize(titleToCheck))
        //assert
            .doesNotThrowAnyException();
    }


    // ------------------- title already exists ------------------
    @Test
    @DisplayName("It should return FALSE because title is unique")
    public void itShouldReturnFalseBecauseTitleIsUnique(){
        //arrange
        String titleToCheck = "someTitle";

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.taskRepository.findByTitle(titleToCheck, authenticatedPerson.getId())).thenReturn(Optional.empty());
        //act
        Boolean methodResult = this.titleValidations.titleAlreadyExists(titleToCheck);
        //assert
            Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should throw an exception because the authenticated person already has this title")
    public void itShouldThrowAnExceptionBecauseTheAuthenticatedPersonAlreadyHasThisTitle(){
        //arrange
        String titleToCheck = "someTitle";

        Task samTask = new Task();
        samTask.setId(1l);
        samTask.setTitle(titleToCheck);
        samTask.setOwner(authenticatedPerson);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.taskRepository.findByTitle(titleToCheck, authenticatedPerson.getId())).thenReturn(Optional.of(samTask));
        //act
        Assertions.assertThatThrownBy(() -> this.titleValidations.titleAlreadyExists(titleToCheck))
        //assert
            .isExactlyInstanceOf(TitleAttributeIsWrongException.class)
            .hasMessageContaining("You already has a task with this title");
    }

    @Test
    @DisplayName("It should return FALSE because, even the task already exists, that task is from another person. So, it's completely fine to create that task. For this authenticated person, this title is unique.")
    public void itShouldReturnFalseBecauseTheAuthenticatedPersonDoesNotHaveThisTaskTitleYet(){
        //arrange
        String titleToCheck = "tileFromAnotherPerson";

        Person anotherPerson = new Person();
        anotherPerson.setId(2l);
        anotherPerson.setUsername("user2");

        Task samTask = new Task();
        samTask.setId(1l);
        samTask.setTitle(titleToCheck);
        samTask.setOwner(anotherPerson);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.taskRepository.findByTitle(titleToCheck, authenticatedPerson.getId())).thenReturn(Optional.of(samTask));
        //act
        Boolean methodResult = this.titleValidations.titleAlreadyExists(titleToCheck);
        //assert
            Assertions.assertThat(methodResult).isFalse();
    }


    // ------------------- title is fully correct ------------------
    @Test
    @DisplayName("It should NOT throw an exception because title is fully correct")
    public void itShouldNotThrowAnExceptionBecauseTitleIsFullyCorrect(){
        //arrange
        String titleToCheck = "correct title";
        Long ownerId = this.authenticatedPerson.getId();

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.taskRepository.findByTitle(titleToCheck, ownerId)).thenReturn(Optional.empty());
        //act
        Assertions.assertThatCode(() -> this.titleValidations.titleIsFullyCorrect(titleToCheck)) 
        //assert
            .doesNotThrowAnyException();
    }
}