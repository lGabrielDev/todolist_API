package br.com.lGabrielDev.todolist_project.models.person.validations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.lGabrielDev.todolist_project.exceptions.PasswordIsWrongException;

public class PasswordValidationsTest {

    //attributes
    private PasswordValidations passValidation; //we are going to test methods from this Class <--
    String passwordToCheck;

    @BeforeEach
    public void startSetup(){
        passValidation = new PasswordValidations();
        
    }

    // ========================== password is null ==========================
    @Test
    @DisplayName("It should return false because password is not null")
    public void itShouldReturnFalseBecausePasswordIsNotNull(){
        //arrange
        this.passwordToCheck = "123";
        //act
        Boolean methodResult = this.passValidation.passwordIsNull(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should return true because password is null")
    public void itShouldReturnTrueBecausePasswordIstNull(){
        //arrange
        this.passwordToCheck = null;
        //act
        Boolean methodResult = this.passValidation.passwordIsNull(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }


    // ========================== password contains between 8 and 20 characters ==========================
    @Test
    @DisplayName("It should return true because the password has the perfect length (8-20)")
    public void itShouldReturnTrueBecausePasswordHasThePerfectLength(){
        //arrange
        this.passwordToCheck = "123456789"; //9 characters
        //act
        Boolean methodResult = this.passValidation.passwordContainsThePerfectLength(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false becasue the password length is less than 8 characters")
    public void itShoulReturnFalseBecausePasswordLengthIsLessThan8(){
        //arrange
        this.passwordToCheck = "12"; //only 2 characters
        //act
        Boolean methodResult = this.passValidation.passwordContainsThePerfectLength(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should return false becasue the password length is greater than 20 characters")
    public void itShouldReturnFalseBecausePasswordLengthIsGreaterThan20(){
        //arrange
        this.passwordToCheck = "password123456789abcd"; //21 characters
        //act
        Boolean methodResult = this.passValidation.passwordContainsThePerfectLength(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== password contains white spaces ==========================
    @Test
    @DisplayName("It should return true because the password contains 'white space'")
    public void itShouldReturnTrueBecausePasswordContainsWhiteSpace(){
        //arrange
        this.passwordToCheck = "a b 123"; //contains 2 white spaces
        //act
        Boolean methodResult = this.passValidation.passwordContainsWhiteSpace(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because the password does not contains 'white space'")
    public void itShouldReturnFalseBecausePasswordDOesNotContainsWhiteSpaces(){
        //arrange
        this.passwordToCheck = "abc123"; //there is no white spaces
        //act
        Boolean methodResult = this.passValidation.passwordContainsWhiteSpace(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== password contains UPPERCASE letters ==========================
    @Test
    @DisplayName("It should return true because password contains UPPERCASE letters")
    public void itShouldReturnTrueBecausePasswordContainsUppercaseLetters(){
        //arrange
        this.passwordToCheck = "pAssWord123"; // 'A' and 'W' are Uppercase letters
        //act
        Boolean methodResult = this.passValidation.passwordContainsUpperCaseLetter(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because password does not have uppercase letters")
    public void itShouldReturnFalseBecausePasswordDoesNotHaveUppercaseLetters(){
        //arrange
        this.passwordToCheck = "abc123"; //there is no uppercase letter
        //act
        Boolean methodResult = this.passValidation.passwordContainsUpperCaseLetter(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== password contains numbers ==========================
    @Test
    @DisplayName("It should return true because password contains numbers")
    public void itShouldReturnTrueBecausePasswordContainsNumbers(){
        //arrange
        this.passwordToCheck = "abc123"; //"123" are numbers
        //act
        Boolean methodResult = this.passValidation.passwordContainsNumbers(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because password does not have numbers")
    public void itShouldReturnFalseBecausePasswordDoesNotHaveNumbers(){
        //arrange
        this.passwordToCheck = "abc@!$"; //there is no numbers
        //act
        Boolean methodResult = this.passValidation.passwordContainsNumbers(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should return false because password contains only letters")
    public void itShouldReturnFalseBecausePasswordContainsOnlyLetters(){
        //arrange
        this.passwordToCheck = "abc"; //there is no numbers
        //act
        Boolean methodResult = this.passValidation.passwordContainsNumbers(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should return false because password contains only special characters")
    public void itShouldReturnFalseBecausePasswordContainsOnlySpecialCharacters(){
        //arrange
        this.passwordToCheck = "@@#@!@!!!%%¨¨&..+*"; //there is no numbers
        //act
        Boolean methodResult = this.passValidation.passwordContainsNumbers(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    // ========================== password contains 2 special characters ==========================
    @Test
    @DisplayName("It should return true because password contains more than 2 special characters")
    public void itShouldReturnTrueBecausePasswordContainsMoreThan2SpecialCharacters(){
        //arrange
        this.passwordToCheck = "123@123!132$"; // 3 special characters --> '@', '!' and '$'
        //act
        Boolean methodResult = this.passValidation.passwordContains2SpecialCharacters(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return true because password contains exactly 2 special characters")
    public void itShouldReturnTrueBecausePasswordContains2SpecialCharacter(){
        //arrange
        this.passwordToCheck = "123@$"; // 2 special characters --> '@', '$'
        //act
        Boolean methodResult = this.passValidation.passwordContains2SpecialCharacters(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    @Test
    @DisplayName("It should return false because password contains only 1 special character")
    public void itShouldReturnFalseBecausePasswordContainsOnly1SpecialCharacter(){
        //arrange
        this.passwordToCheck = "123@123"; // 1 special characters --> '@'
        //act
        Boolean methodResult = this.passValidation.passwordContains2SpecialCharacters(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }

    @Test
    @DisplayName("It should return false because password does not have any special character")
    public void itShouldReturnFalseBecausePasswordDoesNotHaveAnySpecialCharacter(){
        //arrange
        this.passwordToCheck = "abc123"; // there is no special characters
        //act
        Boolean methodResult = this.passValidation.passwordContains2SpecialCharacters(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isFalse();
    }


    // ========================== Password passed through all the validations. So, password is correct ==========================
    @Test
    @DisplayName("It should return true because the password is fully correct")
    public void itShouldReturnTrueBecausePasswordIsFullyCorrect(){
        //arrange
        this.passwordToCheck = "abcABC123!@#"; //9 characters
        //act
        Boolean methodResult = this.passValidation.isPasswordFullyCorrect(passwordToCheck);
        //assert
        Assertions.assertThat(methodResult).isTrue();
    }

    //password is null
    @Test
    @DisplayName("It should throw an exception because password is null")
    public void itShouldThrowAnExceptionBecausePasswordIsNull(){
        //arrange
        this.passwordToCheck = null;
        //act
        Assertions.assertThatThrownBy(() -> this.passValidation.isPasswordFullyCorrect(passwordToCheck))
        //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("'Password' cannot be null");
    }

    //password contains the perfect length
    @Test
    @DisplayName("It should throw an exception because password length is less than 8")
    public void itShouldThrowAnExceptionBecausePasswordLengthIsLessThan8Characters(){
        //arrange
        this.passwordToCheck = "123"; //3 characters
        //act
        Assertions.assertThatThrownBy(() -> this.passValidation.isPasswordFullyCorrect(passwordToCheck))
        //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("'Password' must have between 8 and 20 characters");
        
    }

    //password contains white space
    @Test
    @DisplayName("It should throw an exception because password contains white space")
    public void itShouldThrowAnExceptionBecausePasswordContainsWhiteSpace(){
        //arrange
        this.passwordToCheck = "abc 123 abc"; //2 white spaces
        //act
        Assertions.assertThatThrownBy(() -> this.passValidation.isPasswordFullyCorrect(passwordToCheck))
        //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("Password' cannot have white space");
    }

    //password contains upper case letter
    @Test
    @DisplayName("It should throw an exception because password does not have uppercase letters")
    public void itShouldThrowAnExceptionBecausePasswordDoesNotHaveUppercaseLetters(){
        //arrange
        this.passwordToCheck = "abcabc123@@"; //there is no uppercase letters
        //act
        Assertions.assertThatThrownBy(() -> this.passValidation.isPasswordFullyCorrect(passwordToCheck))
        //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("Password' must have at least 1 UPPERCASE LETTER");
    }

    //password contains numbers
    @Test
    @DisplayName("It should throw an exception because password does not have any number")
    public void itShouldThrowAnExceptionBecausePasswordDoesNotHaveAnyNumber(){
        //arrange
        this.passwordToCheck = "abcABC@#$"; //there is no number
        //act
        Assertions.assertThatThrownBy(() -> this.passValidation.isPasswordFullyCorrect(passwordToCheck))
        //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("Password' must have at least 1 number");
    }

    //password contains 2 special characters
    @Test
    @DisplayName("It should throw an exception because password does not have 2 special characters")
    public void itShouldThrowAnExceptionBecausePasswordDoesNotHave2SpecialCharacters(){
        //arrange
        this.passwordToCheck = "abc123ABC123"; //there is no special characters
        //act
        Assertions.assertThatThrownBy(() -> this.passValidation.isPasswordFullyCorrect(passwordToCheck))
        //assert
            .isExactlyInstanceOf(PasswordIsWrongException.class)
            .hasMessageContaining("Password' must have 2 special character");
    }
}