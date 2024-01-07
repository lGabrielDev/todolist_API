package br.com.lGabrielDev.todolist_project.models.task.validations;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.PriorityAttributeIsWrongException;

public class PriorityValidationsTest {
    
    //injeted attributes
    private PriorityValidations priorityValidations;
    private Integer priorityToCheck;

    @BeforeEach
    public void setUp(){
        this.priorityValidations = new PriorityValidations();
    }

    // --- priority is correct ---
    @Test
    @DisplayName("It should NOT get any exception because priority is correct and equal to 1")
    public void itShouldNotGetAnyExceptionBeacusePriorityIsCorrectAndEquals1(){
        //arrange
        this.priorityToCheck = 1;
        //act + assert
        Assertions.assertThatCode(() -> this.priorityValidations.isPriorityValid(priorityToCheck))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should NOT get any exception because priority is correct and equal to 2")
    public void itShouldNotGetAnyExceptionBeacusePriorityIsCorrectAndEquals2(){
        //arrange
        this.priorityToCheck = 2;
        //act + assert
        Assertions.assertThatCode(() -> this.priorityValidations.isPriorityValid(priorityToCheck))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should NOT get any exception because priority is correct and equal to 3")
    public void itShouldNotGetAnyExceptionBeacusePriorityIsCorrectAndEquals3(){
        //arrange
        this.priorityToCheck = 3;
        //act + assert
        Assertions.assertThatCode(() -> this.priorityValidations.isPriorityValid(priorityToCheck))
            .doesNotThrowAnyException();
    }


    // --- priority is NOT correct ---
    @Test
    @DisplayName("It should get an exception because priority IS NULL")
    public void itShouldThrowAnExceptionBecausePriorityIsNull(){
        //arrange
        this.priorityToCheck = null;
        //act + assert 
        Assertions.assertThatThrownBy(() -> this.priorityValidations.isPriorityValid(priorityToCheck))
        .isExactlyInstanceOf(PriorityAttributeIsWrongException.class)
        .hasMessageContaining("'priority' must be 1, 2 or 3");
    }

    @Test
    @DisplayName("It should get an exception because priority is NOT between 1,2 or 3")
    public void itShouldThrowAnExceptionBecausePriorityIsNotBetween1and2and3(){
        //arrange
        this.priorityToCheck = 4; //not 1,2 or 3
        //act + assert 
        Assertions.assertThatThrownBy(() -> this.priorityValidations.isPriorityValid(priorityToCheck))
        .isExactlyInstanceOf(PriorityAttributeIsWrongException.class)
        .hasMessageContaining("'priority' must be 1, 2 or 3");
    }
}