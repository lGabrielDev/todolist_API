package br.com.lGabrielDev.todolist_project.models.task.validations;

import static org.mockito.ArgumentMatchers.anyLong;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.CategoryAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;

@ExtendWith(MockitoExtension.class)
public class CategoryIdValidationsTest {

    //injected attributes
    @InjectMocks
    private CategoryIdValidations categoryIdValidations;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthenticationMethods authenticationMethods;

    private Person authenticatedPerson;


    @BeforeEach
    public void setUp(){
        this.authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("user1");
    }

    // ---------------- category_id cannot be null ----------------
    @Test
    @DisplayName("It should not get an exception because the 'categoryId' is correct")
    public void itShouldNotGetAnExceptionBecauseCategoryIdIsCorrect(){
        //arrange
        Long categoryId = 1l;
        //act + assert
        Assertions.assertThatCode(() -> this.categoryIdValidations.categoryIdIsNull(categoryId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should get an exception because the 'categoryId' is null")
    public void itShouldGetAnExceptionBecauseCategoryIdIsNull(){
        //arrange
        Long categoryId = null;
        //act + //assert
        Assertions.assertThatThrownBy(() -> this.categoryIdValidations.categoryIdIsNull(categoryId))
        
            .isExactlyInstanceOf(CategoryAttributeIsWrongException.class)
            .hasMessageContaining("'categoryId' cannot be null");
    }


    // ---------------- category doesn't exists ----------------
    @Test
    @DisplayName("It should throw an exception because categoryId does not exists")
    public void itShouldGetAnExceptionBecauseCategoryIdDoesNotExists(){
        //arrange
        Long categoryId = 1l;
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.categoryRepository.existsById(categoryId)).thenReturn(false);
        //act
        Assertions.assertThatThrownBy(() -> this.categoryIdValidations.categoryDoesNotExistsShowAllAvailableCategories(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryAttributeIsWrongException.class);
            Mockito.verify(this.authenticationMethods, Mockito.times(1)).getTheAuthenticatedPerson();
            Mockito.verify(this.categoryRepository, Mockito.times(1)).existsById(anyLong());
            Mockito.verify(this.categoryRepository, Mockito.times(1)).findAll(anyLong());     
    }

    @Test
    @DisplayName("It should do nothing because categoryId is correct")
    public void itShouldDoNothingBecauseCategoryIdIsCorrect(){
        //arrange
        Long categoryId = 1l;

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("user1");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.when(this.categoryRepository.existsById(categoryId)).thenReturn(true);
        //act + assert
        Assertions.assertThatCode(() -> this.categoryIdValidations.categoryDoesNotExistsShowAllAvailableCategories(categoryId))
            .doesNotThrowAnyException();
    }


    // ---------------- category_id if fully correct ----------------
    @Test
    @DisplayName("It does not throws an exception because categoryId is correct")
    public void itShouldNotGetAnExceptionBecauseCategoryIdIsFullyCorrect(){
        //arrange
        Long categoryId = 1l;

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(this.authenticatedPerson);
        Mockito.when(this.categoryRepository.existsById(categoryId)).thenReturn(true);
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(categoryId);
        //act
        Assertions.assertThatCode(() -> this.categoryIdValidations.categoryIdIsFullyCorrect(categoryId))
        //assert
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("It should get an exception because categoryId is null")
    public void isNotFullyCorrectSoItShouldGetAnException(){
        //arrange
        Long categoryId = null;
        //act
        Assertions.assertThatThrownBy(() -> this.categoryIdValidations.categoryIdIsFullyCorrect(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryAttributeIsWrongException.class);
    }
}