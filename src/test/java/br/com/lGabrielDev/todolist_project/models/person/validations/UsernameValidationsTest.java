package br.com.lGabrielDev.todolist_project.models.person.validations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.lGabrielDev.todolist_project.exceptions.UsernameIsWrong;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsernameValidationsTest {
    
    //attributes
    private String usernameToCheck;

    @InjectMocks
    private UsernameValidations userValidations;

    @Mock
    private PersonRepository personRepository; //we going to "mock"/fake the methods of this Class


    // ========================== username cannot be null ==========================
    @Test
    @DisplayName("It should return true because username is null")
    public void itShouldReturnTrueBecauseUsernameIsNull(){
        //arrange
        usernameToCheck = null;
        //act
        Boolean methodResult = this.userValidations.usernameIsNull(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because username IS NOT null")
    public void itShouldReturnFalseBecauseUsernameIsNotNull(){
        //arrange
        usernameToCheck = "123";
        //act
        Boolean methodResult = this.userValidations.usernameIsNull(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== username cannot have white space ==========================
    @Test
    @DisplayName("It should return true because username contains white spaces")
    public void itShouldReturnTrueBecauseUsernameContainsWhiteSpaces(){
        //arrange
        usernameToCheck = "abc 123 abc"; //2 white spaces
        //act
        Boolean methodResult = this.userValidations.usernameContainsWhiteSpace(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because username does not contains white spaces")
    public void itShouldReturnFalseBecauseUsernameDoesNotContainsWhiteSpaces(){
        //arrange
        this.usernameToCheck = "abc123"; //0 white spaces
        //act
        Boolean methodResult = this.userValidations.usernameContainsWhiteSpace(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== username must have between 5 and 20 characters ==========================
    @Test
    @DisplayName("It should return true because username length is between 5 and 20 characters")
    public void itShouldReturnTrueBecauseUsernameHasThePerfectLength(){
        //arrange
        this.usernameToCheck = "sonic123"; //8 characters
        //act
        Boolean methodResult = this.userValidations.usernameContainsThePerfectLength(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because username contains less than 5 characters")
    public void itShouldReturnFalseBecauseUsernameContainsLessThan5Characters(){
        //arrange
        this.usernameToCheck = "so12"; //4 characters
        //act
        Boolean methodResult = this.userValidations.usernameContainsThePerfectLength(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should return false because username contains more than 20 characters")
    public void itShouldReturnFalseBecauseUsernameContainsMoreThan20Characters(){
        //arrange
        this.usernameToCheck = "1234567891abcabcabc12"; //21 characters
        //act
        Boolean methodResult = this.userValidations.usernameContainsThePerfectLength(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== username already exists ==========================
    @Test
    @DisplayName("It should return true because username already exists")
    public void itShouldReturnTrueBecauseUsernameAlreadyExists(){
        //arrange
        this.usernameToCheck = "sonic";

        Person personFound = new Person(usernameToCheck, "123");

        Mockito.when(this.personRepository.findByUsername(usernameToCheck)).thenReturn(Optional.of(personFound));

        //act
        Boolean methodResult = this.userValidations.usernameAlreadyExists(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because username is unique, ready to be created")
    public void itShouldReturnFalseBecauseUsernameIsUnique(){
        //arrange
        this.usernameToCheck = "sonic";

        Mockito.when(this.personRepository.findByUsername(usernameToCheck)).thenReturn(Optional.empty());

        //act
        Boolean methodResult = this.userValidations.usernameAlreadyExists(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== username is fully correct ==========================
    @Test
    @DisplayName("It should return true because username is fully correct")
    public void itShouldReturnTrueBecauseUsernameIsFullyCorrect(){
        //arrange
        String usernameToCheck = "sonic";
        //act
        Boolean methodResult = this.userValidations.isUsernameFullyCorrect(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    //username is null exception
    @Test
    @DisplayName("It should throw an exception because username is null")
    public void itShouldThrowAnExceptionBecauseUsernameIsNull(){
        //arrange
        String usernameToCheck = null;
        //act
        Assertions.assertThatThrownBy(() -> this.userValidations.isUsernameFullyCorrect(usernameToCheck))
        //assert
            .isExactlyInstanceOf(UsernameIsWrong.class)
            .hasMessageContaining("'username' cannot be null");
    }

    //username cannot have white spaces
    @Test
    @DisplayName("It should throw an exception because username contains white spaces")
    public void itShouldThrowAnExceptionBecauseUsernameContainsWhiteSpaces(){
        //arrange
        String usernameToCheck = "123 abc 123"; //2 white spaces
        //act
        Assertions.assertThatThrownBy(() -> this.userValidations.isUsernameFullyCorrect(usernameToCheck))
        //assert
            .isExactlyInstanceOf(UsernameIsWrong.class)
            .hasMessageContaining("'username' cannot have white spaces");
    }

    //username must have between 5 and 20 characters
    @Test
    @DisplayName("It should throw an exception because username is greater than 20 characters")
    public void itShouldThrowAnExceptionBecauseUsernameLengthIsGreaterThan20Characters(){
        //arrange
        String usernameToCheck = "abcabcabcabcabcabcabcabcabcabcabcabcabc"; //a lot of characters
        //act
        Assertions.assertThatThrownBy(() -> this.userValidations.isUsernameFullyCorrect(usernameToCheck))
        //assert
            .isExactlyInstanceOf(UsernameIsWrong.class)
            .hasMessageContaining("'username' must have between 5 and 20 characters");
    }
    
    //username is not unique
    @Test
    @DisplayName("It should throw an exception because username is not unique")
    public void itShouldThrowAnExceptionBecauseUsernameIsNotUnique(){
        //arrange
        String usernameToCheck = "sonic";

        Person person = new Person(usernameToCheck, "123");

        Mockito.when(this.personRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(person));

        //act
        Assertions.assertThatThrownBy(() -> this.userValidations.isUsernameFullyCorrect(usernameToCheck))
        //assert
            .isExactlyInstanceOf(UsernameIsWrong.class)
            .hasMessageContaining("username' is not unique. Already exists a person with this 'username'");
    }
}