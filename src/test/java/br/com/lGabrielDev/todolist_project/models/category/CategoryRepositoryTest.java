package br.com.lGabrielDev.todolist_project.models.category;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;

@DataJpaTest
public class CategoryRepositoryTest {
    
    //injected attributes
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PersonRepository personRepository;

    private Person ownerOfAll;
    private Category category1;
    private Category category2;

    @BeforeEach
    public void setUp(){
        this.ownerOfAll = new Person();
        this.ownerOfAll.setUsername("ownerOfAll");
        this.personRepository.save(ownerOfAll);

        this.category1 = new Category();
        this.category1.setName("category1");
        this.category1.setOwner(ownerOfAll);

        this.category2 = new Category();
        this.category2.setName("category2");
        this.category2.setOwner(ownerOfAll);
    }

    // -------------- read all, filtered by person #ID -----------------
    @Test
    @DisplayName("It should return a list of categories, filtered by person #ID")
    public void itShouldReturnAListOfCategoriesFilteredByPersonIdSucessfully(){
        //arrange
        this.categoryRepository.save(this.category1);
        this.categoryRepository.save(this.category2);
        //act
        List<Category> methodResult = this.categoryRepository.findAll(this.ownerOfAll.getId());
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.size()).isEqualTo(2);
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(this.category1.getId());
        Assertions.assertThat(methodResult.get(0).getName()).isEqualTo(this.category1.getName());
        Assertions.assertThat(methodResult.get(1).getId()).isEqualTo(this.category2.getId());
        Assertions.assertThat(methodResult.get(1).getName()).isEqualTo(this.category2.getName());
    }

    @Test
    @DisplayName("It should return an empty list, becasue there is no category with that person #ID")
    public void itShouldReturnAnEmptyListBecauseThereIsNoCategoryWithThatPersonId(){
        //arrange
        Long ownerIdToFind = 33l;
        this.categoryRepository.saveAll(List.of(this.category1, this.category2));
        //act
        List<Category> methodResult = this.categoryRepository.findAll(ownerIdToFind);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }


    // -------------- find by 'category_name', filtered by owner_id -----------------
    @Test
    @DisplayName("It should return a list of categories, filtered by 'category name' AND 'owner_id'")
    public void itShouldReturnAlistOfCategoriesFilteredByCategoryNameAndOwnerIdSucessfully(){
        //arrange
        Long ownerIdToFind = this.ownerOfAll.getId();
        this.ownerOfAll.getCategories().add(category1);
        this.ownerOfAll.getCategories().add(category2); 

        this.personRepository.save(ownerOfAll);

        this.categoryRepository.saveAll(List.of(category1,category2));
        //act
        Optional<Category> methodResult = this.categoryRepository.findByName("category1", ownerIdToFind);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).isPresent();
        Assertions.assertThat(methodResult.get().getId()).isEqualTo(category1.getId());
        Assertions.assertThat(methodResult.get().getName()).isEqualTo(category1.getName()); 
    }

    @Test
    @DisplayName("It should return an empty list, because there is no category with those filters")
    public void itShouldReturnAnEmptyListBecauseThereIsNotCategoryWithThoesFilters(){
        //arrange
        Long ownerIdToFind = 1l;
        String categoryName = "something";
        //act
        Optional<Category> methodResult = this.categoryRepository.findByName(categoryName, ownerIdToFind);
        //assert
        Assertions.assertThat(methodResult).isNotPresent();
    }
}