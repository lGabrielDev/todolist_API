package br.com.lGabrielDev.todolist_project.models.category.dtos;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.person.Person;

public class CategoryWithIdNameAndOwnerIdDtoTest {

    //It should return a list successfully
    @Test
    @DisplayName("It should return a list of 'CategoryWithIdNameAndOwnerIdDto' successfully")
    public void itShouldTransformAlistOfCategoryIntoAListOfCategoryWithIdNameAndOwnerIdDtoSuccessFully(){
        //arrange
        Person owner = new Person();
        owner.setId(33l);
        owner.setUsername("sonic");

        Category sportCategory = new Category();
        sportCategory.setId(1l);
        sportCategory.setName("sport");
        sportCategory.setOwner(owner);
        List<Category> listFromDb = List.of(sportCategory);

        CategoryWithIdNameAndOwnerIdDto dto = new CategoryWithIdNameAndOwnerIdDto(sportCategory);
        List<CategoryWithIdNameAndOwnerIdDto> expectedResult = List.of(dto);
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = CategoryWithIdNameAndOwnerIdDto.transformList(listFromDb);
        //assert
        Assertions.assertThat(methodResult).isEqualTo(expectedResult);
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.size()).isEqualTo(1);
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getName()).isEqualTo("sport");
        Assertions.assertThat(methodResult.get(0).getOwnerId()).isEqualTo(33);
    }
    
    //It should return an empty list
    @Test
    @DisplayName("It should return an empty list")
    public void itShouldReturnAnEmptyList(){
        //arrange
        List<Category> listFromDb = new ArrayList<>();
        //act
        List<CategoryWithIdNameAndOwnerIdDto> methodResult = CategoryWithIdNameAndOwnerIdDto.transformList(listFromDb);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }
}