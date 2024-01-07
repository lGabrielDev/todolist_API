package br.com.lGabrielDev.todolist_project.models.category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryCreateDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryReadOneDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryWithIdNameAndOwnerIdDto;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryContainsTasksException;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryIsFromAnotherPersonException;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNameIsWrong;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNotFoundException;
import br.com.lGabrielDev.todolist_project.models.category.validations.CategoryNameValidations;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    
    //injected attributes
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryNameValidations categoryNameValidations;

    @Mock
    private AuthenticationMethods authenticationMethods;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PersonRepository personRepository;

      
    // ------------------------- CREATE -------------------------
    @Test
    @DisplayName("It should create a category sucessfully")
    public void itShouldCreateACategorySuccessfully(){
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("category something");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("sonic");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);

        Category categoryToBeCreated = new Category(categoryDto);
        categoryToBeCreated.setId(12l);
        categoryToBeCreated.setOwner(authenticatedPerson);

        Mockito.when(this.categoryRepository.save(Mockito.any(Category.class))).thenReturn(categoryToBeCreated);
        Mockito.when(this.personRepository.save(Mockito.any(Person.class))).thenReturn(authenticatedPerson);

        List<Category> listToConvert = new ArrayList<>();
        listToConvert.add(categoryToBeCreated);

        Mockito.when(this.categoryRepository.findAll(Mockito.anyLong())).thenReturn(listToConvert);
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = this.categoryService.createCategory(categoryDto);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.size()).isEqualTo(1);
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(12);
        Assertions.assertThat(methodResult.get(0).getName()).isEqualTo("category something");
    }

    @Test
    @DisplayName("It should get an exception because 'category name' is null")
    public void itShouldGetAnExceptionBecauseCategoryNameIsNull(){
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("categorySomething");
    
        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(new Person());

        Mockito.doThrow(new CategoryNameIsWrong("'category name' cannot be null"))
            .when(this.categoryNameValidations).categoryNameIsNull(Mockito.anyString());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.createCategory(categoryDto))
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class)
            .hasMessageContaining("'category name' cannot be null");
    }

    @Test
    @DisplayName("It should get an exception because 'category name' has more than 20 characters")
    public void itShouldGetAnExceptionBecauseCategoryNameHasMoreThan20Characters(){
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("categorySomething");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("sonic");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.doNothing().when(this.categoryNameValidations).categoryNameIsNull(anyString());
        
        Mockito.doThrow(new CategoryNameIsWrong("'Category name' has more than 20 characteres"))
            .when(this.categoryNameValidations).hasThePerfectLength(anyString());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.createCategory(categoryDto)) 
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class)
            .hasMessageContaining("'Category name' has more than 20 characteres");
    }

    @Test
    @DisplayName("It should throw an exception because 'category name' already exists")
    public void itShouldThrowExceptionWhenCategoryNameAlreadyExists(){
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("categorySomething");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("sonic");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.doNothing().when(this.categoryNameValidations).categoryNameIsNull(Mockito.anyString());
        Mockito.doNothing().when(this.categoryNameValidations).hasThePerfectLength(Mockito.anyString());
        
        Mockito.doThrow(new CategoryNameIsWrong("Category name already exists"))
            .when(this.categoryNameValidations).personAlreadyHasThisCategoryName(Mockito.anyString(), Mockito.anyLong());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.createCategory(categoryDto))
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class)
            .hasMessageContaining("Category name already exists");
    }


    // ------------------------- READ ALL -------------------------
    @Test
    @DisplayName("It should get a list of categories with 2 categories on it")
    public void itShouldGetAListOfCategoriesWith2CategoriesOnIt(){
        //arrnage
        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("sonic");

        Person ownerOfAll = new Person();
        ownerOfAll.setId(1l);
        ownerOfAll.setUsername("userSomething");

        Category category1 = new Category();
        category1.setName("category1");
        category1.setOwner(ownerOfAll);

        Category category2 = new Category();
        category2.setName("category2");
        category2.setOwner(ownerOfAll);

        List<Category> expectedResult = List.of(category1, category2);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.when(this.categoryRepository.findAll(anyLong())).thenReturn(expectedResult);
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = this.categoryService.readAllCategories();
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.size()).isEqualTo(2);
        Assertions.assertThat(methodResult.get(0).getName()).isEqualTo("category1");
        Assertions.assertThat(methodResult.get(1).getName()).isEqualTo("category2");
    }

    @Test
    @DisplayName("It should get an empty list of categories")
    public void itShouldGetAnEmptyListOfCategories(){
        //arrnage
        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("sonic");

        List<Category> expectedResult = new ArrayList<>();

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.when(this.categoryRepository.findAll(anyLong())).thenReturn(expectedResult);
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = this.categoryService.readAllCategories();
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    
    // ------------------------- READ ONE -------------------------
    @Test
    @DisplayName("It should get a category by categoryId successfully")
    public void itShouldGetACategoryByCategoryIdSuccessfully(){
        //arrange
        Person owner = new Person();
        owner.setId(1l);
        owner.setUsername("userSomething");

        Category categoryWeWantToFind = new Category();
        categoryWeWantToFind.setId(1l);
        categoryWeWantToFind.setName("category1");
        categoryWeWantToFind.setOwner(owner);

        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        Mockito.when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryWeWantToFind));
        //act
        CategoryReadOneDto methodResult = this.categoryService.readOneCategory(categoryWeWantToFind.getId());
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.getName()).isEqualTo(categoryWeWantToFind.getName());
    }

    @Test
    @DisplayName("It should get an exception because category doesn't exists")
    public void itShouldGetAnExcetionBecauseCategoryDoesntExists(){
        //arrange
        Long categoryId = 1l;

        Mockito.doThrow(new CategoryNotFoundException("Category not found"))
            .when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.readOneCategory(categoryId))
        //assert
        .isExactlyInstanceOf(CategoryNotFoundException.class)
        .hasMessageContaining("Category not found"); 
    }

    @Test
    @DisplayName("It should get an exception because category belongs to another person")
    public void readOneItShouldGetAnExcetionBecauseCategoryBelongsToAnotherPerson(){
        //arrange
        Long categoryId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());

        Mockito.doThrow(new CategoryNotFoundException("Category belongs to another person"))
            .when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.readOneCategory(categoryId))
        //assert
        .isExactlyInstanceOf(CategoryNotFoundException.class)
        .hasMessageContaining("Category belongs to another person"); 
    }


    // ------------------------- UPDATE -------------------------
    @Test
    @DisplayName("It should update a category successfully")
    public void itShouldUpdateACategorySuccessfully(){
        //arrange
        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("userSomething");
        
        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("category1");

        Category categoryWeWantToUpdate = new Category(categoryDto);
        categoryWeWantToUpdate.setId(1l);
        categoryWeWantToUpdate.setOwner(authenticatedPerson);

        Long categoryId = categoryWeWantToUpdate.getId();

        List<Category> expectedListResult = List.of(categoryWeWantToUpdate);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        Mockito.when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryWeWantToUpdate));

        Mockito.doNothing().when(this.categoryNameValidations).hasThePerfectLength(anyString());
        Mockito.doNothing().when(this.categoryNameValidations).personAlreadyHasThisCategoryName(anyString(), anyLong());
        
        Mockito.when(this.categoryRepository.save(Mockito.any(Category.class))).thenReturn(categoryWeWantToUpdate);
        Mockito.when(this.categoryRepository.findAll(anyLong())).thenReturn(expectedListResult);
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = this.categoryService.updateCategory(categoryId, categoryDto);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.get(0).getName()).isEqualTo("category1");
        //mocks verify
        Mockito.verify(this.authenticationMethods, Mockito.times(1)).getTheAuthenticatedPerson();
        Mockito.verify(this.authenticationMethods, Mockito.times(1)).checkIfCategoryExists(anyLong());
        Mockito.verify(this.authenticationMethods, Mockito.times(1)).checkIfCategoryIsFromAnotherPerson(anyLong());
        Mockito.verify(this.categoryRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(this.categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
        Mockito.verify(this.categoryRepository, Mockito.times(1)).findAll(Mockito.anyLong());
    }

    @Test
    @DisplayName("It should get an exception because category id doesn't exists")
    public void itShouldGetAnExceptionBecauseCategoryIdDoesntExists(){
        //arrange
        Long categoryId = 1l;

        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("category1");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("userSomething");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);

        Mockito.doThrow(new CategoryNotFoundException("category not found"))
            .when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.updateCategory(categoryId, categoryDto))
        //assert
            .isExactlyInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining("category not found");
    }

    @Test
    @DisplayName("It should get an exception because category belongs to another person")
    public void updateItShouldGetAnExceptionBecauseCategoryBelongsToAnotherPerson(){
        //arrange
        Long categoryId = 1l;

        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("category1");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("userSomething");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());

        Mockito.doThrow(new CategoryNotFoundException("category belongs to another person"))
            .when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.updateCategory(categoryId, categoryDto))
        //assert
            .isExactlyInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining("category belongs to another person");
    }

    @Test
    @DisplayName("It should get an exception because category name has more than 20 characters")
    public void updateItShouldGetAnExceptionBecauseCategoryNameHasMoreThan20Characters(){
        //arrange
        Long categoryId = 1l;

        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("nameToooooooooooooooooooooooooooooooooooLong");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("userSomething");

        Category categoryFromDb = new Category();
        categoryFromDb.setName("oldName");
        categoryFromDb.setOwner(authenticatedPerson);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        Mockito.when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryFromDb));

        Mockito.doThrow(new CategoryNameIsWrong("'category name' must have between 1 and 20 characters"))
            .when(this.categoryNameValidations).hasThePerfectLength(anyString());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.updateCategory(categoryId, categoryDto))
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class)
            .hasMessageContaining("'category name' must have between 1 and 20 characters");
    }

    @Test
    @DisplayName("It should get an exception because the authenticated person already has this category name")
    public void updateItShouldGetAnExceptionBecauseTheAuthenticatedPersonAlreadyHasThisCategoryName(){
        //arrange
        Long categoryId = 1l;

        CategoryCreateDto categoryDto = new CategoryCreateDto();
        categoryDto.setName("categorySomething");

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("userSomething");

        Category categoryFromDb = new Category();
        categoryFromDb.setName("categorySomething");
        categoryFromDb.setOwner(authenticatedPerson);

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        Mockito.when(this.categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryFromDb));

        Mockito.doNothing().when(this.categoryNameValidations).hasThePerfectLength(anyString());
       
        Mockito.doThrow(new CategoryNameIsWrong("'category name' already exists"))
            .when(this.categoryNameValidations).personAlreadyHasThisCategoryName(anyString(), anyLong());
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.updateCategory(categoryId, categoryDto))
        //assert
            .isExactlyInstanceOf(CategoryNameIsWrong.class)
            .hasMessageContaining("'category name' already exists");
    }


    // ------------------------- DELETE -------------------------
    @Test
    @DisplayName("It should delete a category successfully")
    public void itShouldDeleteACategorySuccessfully(){
        //arrange
        Long categoryId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryContainsTasks(anyLong());
        Mockito.doNothing().when(this.categoryRepository).deleteById(anyLong());

        Person authenticatedPerson = new Person();
        authenticatedPerson.setId(1l);
        authenticatedPerson.setUsername("userSomething");

        Mockito.when(this.authenticationMethods.getTheAuthenticatedPerson()).thenReturn(authenticatedPerson);
        
        List<Category> expectedListResult = new ArrayList<>();
        Mockito.when(this.categoryRepository.findAll(anyLong())).thenReturn(expectedListResult);
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = this.categoryService.deleteCategoryById(categoryId);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    @Test
    @DisplayName("It should get an exception because category doesn't exists")
    public void deleteItShouldGetAnExceptionBecauseCategoryIdDoesntExists(){
        //arrange
        Long categoryId = 1l;

        Mockito.doThrow(new CategoryNotFoundException("category not found"))
            .when(this.authenticationMethods).checkIfCategoryExists(categoryId);
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.deleteCategoryById(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryNotFoundException.class)
            .hasMessageContaining("category not found");
    }

    @Test
    @DisplayName("It should get an exception because category belongs to another person")
    public void deleteItShouldGetAnExceptionBecauseCategoryBelongsToAnotherPerson(){
        //arrange
        Long categoryId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());

        Mockito.doThrow(new CategoryIsFromAnotherPersonException("category belongs to another person"))
            .when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(categoryId);
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.deleteCategoryById(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryIsFromAnotherPersonException.class)
            .hasMessageContaining("category belongs to another person");
    }

    @Test
    @DisplayName("It should get an exception because you cannot delete a category with tasks on it")
    public void deleteItShouldGetAnExceptionBecauseYouCannotDeleteACategoryWithTasksOnIt(){
        //arrange
        Long categoryId = 1l;

        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryExists(anyLong());
        Mockito.doNothing().when(this.authenticationMethods).checkIfCategoryIsFromAnotherPerson(anyLong());

        Mockito.doThrow(new CategoryContainsTasksException("Cannot delete this category because it contains tasks"))
            .when(this.authenticationMethods).checkIfCategoryContainsTasks(categoryId);
        //act
        Assertions.assertThatThrownBy(() -> this.categoryService.deleteCategoryById(categoryId))
        //assert
            .isExactlyInstanceOf(CategoryContainsTasksException.class)
            .hasMessageContaining("Cannot delete this category because it contains tasks");
    }
}