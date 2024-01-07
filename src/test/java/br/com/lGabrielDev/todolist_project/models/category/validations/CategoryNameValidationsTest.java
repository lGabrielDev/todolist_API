package br.com.lGabrielDev.todolist_project.models.category.validations;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNameIsWrong;

@ExtendWith(MockitoExtension.class)
public class CategoryNameValidationsTest {

    //attributes
    @InjectMocks
    private CategoryNameValidations categoryNameValidations;

    @Mock
    private CategoryRepository categoryRepository;

    // ----- 'category name' cannot be null -----
    @Test
    @DisplayName("It should get an exception because 'category name' is null")
    public void itShouldGetAnExceptionBecauseCategoryNameIsNull(){
        //arrange
        String categoryName = null;
        //act
        Assertions.assertThatThrownBy(() -> this.categoryNameValidations.categoryNameIsNull(categoryName))
        //assert
        .isExactlyInstanceOf(CategoryNameIsWrong.class)
        .hasMessageContaining("'category name' cannot be null");
    }

    @Test
    @DisplayName("It should do nothing, because 'category name' IS NOT null")
    public void itShouldDoNothingBecauseCategoryNameIsNotNull(){
        //arrange
        String categoryName = "something";
        //act and assert
        this.categoryNameValidations.categoryNameIsNull(categoryName);
    }


    // ----- 'category name' length between 1 and 20 characters -----
    @Test
    @DisplayName("It should get an exception because 'category name' has more than 20 characters")
    public void itShouldGetAnExceptionBecauseCategoryNameHasMoreThan20Characters(){
        //arrange
        String categoryName = "a tooooooooooo long string"; //26 characters
        //act
        Assertions.assertThatThrownBy(() -> this.categoryNameValidations.hasThePerfectLength(categoryName))
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class);
    }

    @Test
    @DisplayName("It should get an exception because 'category name' has less than 1 characters")
    public void itShouldGetAnExceptionBecauseCategoryNameHasLessThan1Character(){
        //arrange
        String categoryName = ""; //0 characters
        //act
        Assertions.assertThatThrownBy(() -> this.categoryNameValidations.hasThePerfectLength(categoryName))
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class);
    }

    @Test
    @DisplayName("It should do nothing becasue 'category name' has the perfect length")
    public void itSohuldDoNothingBecauseCategoryNameHasThePerfectLength(){
        //arrange
        String categoryName = "something"; //9 characters
        //act and assert
        this.categoryNameValidations.hasThePerfectLength(categoryName);
    }


    // ----- 'category name' already exists -----
    @Test
    @DisplayName("It should get an exception because 'category name' already exists")
    public void itShouldGetAnExceptionBecauseCategoryNameAlreadyExists(){
        //arrange
        Category categoryExists = new Category();
        Mockito.when(this.categoryRepository.findByName(Mockito.anyString(), Mockito.anyLong())).thenReturn(Optional.of(categoryExists));
        //act and assert
        Assertions.assertThatThrownBy(() -> this.categoryNameValidations.personAlreadyHasThisCategoryName(Mockito.anyString(), Mockito.anyLong()))
            .isExactlyInstanceOf(CategoryNameIsWrong.class)
            .hasMessageContaining("You already has a category named");
    }
    
    @Test
    @DisplayName("It should do nothing because 'category name' does not exists yet")
    public void itShouldDoNothingBecauseCategoryNameDoesNotExistsYet(){
        //arrange
        Mockito.when(this.categoryRepository.findByName(Mockito.anyString(), Mockito.anyLong())).thenReturn(Optional.empty());
        //act and assert
        this.categoryNameValidations.personAlreadyHasThisCategoryName(Mockito.anyString(), Mockito.anyLong());
    }
}