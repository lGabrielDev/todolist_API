package br.com.lGabrielDev.todolist_project.models.task.validations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.DescriptionAttributeIsWrongException;

public class DescriptionValidationsTest {

    //injected attributes
    private DescriptionValidations descriptionValidations;

    @BeforeEach
    public void setUp(){
        this.descriptionValidations = new DescriptionValidations();
    }
    
    // ----- description is correct -----
    @Test
    @DisplayName("It does not throws any exception because the description length is correct")
    public void itDoesNotThrowsAnyExceptionBecauseTheDescriptionLengthIsCorrect(){
        //arrange
        String decriptionToCheck = "something something something"; //exactly 29 characters
        //act + assert
        Assertions.assertThatCode(() -> this.descriptionValidations.descriptionHasThePerfectLength(decriptionToCheck))
            .doesNotThrowAnyException();
    }

    // ----- description is NOT correct -----
    @Test
    @DisplayName("It should throw an exception because description IS NULL")
    public void itShouldThrowAnExceptionBecauseDescriptionIsNull(){
        //arrange
        String decriptionToCheck = null;
        //act + assert
        Assertions.assertThatThrownBy(() -> this.descriptionValidations.descriptionHasThePerfectLength(decriptionToCheck))
           .isExactlyInstanceOf(DescriptionAttributeIsWrongException.class);
    }

    @Test
    @DisplayName("It should throw an exception because description length is greater than 100 characters")
    public void itShouldThrowAnExceptionBecauseDescriptionLengthIsGreaterThan100(){
        //arrange
        String decriptionToCheck = "something something something something something something something something something something something something something";
        //act + assert
        Assertions.assertThatThrownBy(() -> this.descriptionValidations.descriptionHasThePerfectLength(decriptionToCheck))
           .isExactlyInstanceOf(DescriptionAttributeIsWrongException.class);
    }
}