package br.com.lGabrielDev.todolist_project.models.category;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryCreateDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryReadOneDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryWithIdNameAndOwnerIdDto;
import br.com.lGabrielDev.todolist_project.models.category.validations.CategoryNameValidations;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;

@Service
public class CategoryService {
    
    //injected attributes
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryNameValidations categoryNameValidations;

    @Autowired
    private AuthenticationMethods authenticationMethods;
    
    @Autowired
    private PersonRepository personRepository;

    // ------------------------- CREATE -------------------------
    public List<CategoryWithIdNameAndOwnerIdDto> createCategory(CategoryCreateDto categoryDto){
        Person authenticatedPerson = authenticationMethods.getTheAuthenticatedPerson();
        //validations
        this.categoryNameValidations.categoryNameIsNull(categoryDto.getName());
        this.categoryNameValidations.hasThePerfectLength(categoryDto.getName());
        this.categoryNameValidations.personAlreadyHasThisCategoryName(categoryDto.getName(), authenticatedPerson.getId());
        Category categoryToCreate = new Category(categoryDto);
        categoryToCreate.setOwner(authenticatedPerson);
        //bilaterally
        authenticatedPerson.getCategories().add(categoryToCreate);
        //saver on db
        this.categoryRepository.save(categoryToCreate);
        this.personRepository.save(authenticatedPerson);
        return CategoryWithIdNameAndOwnerIdDto.transformList(this.categoryRepository.findAll(authenticatedPerson.getId()));
    }

    // ------------------------- READ ALL -------------------------
    public List<CategoryWithIdNameAndOwnerIdDto> readAllCategories(){
        Person authenticatedPerson = authenticationMethods.getTheAuthenticatedPerson();
        return CategoryWithIdNameAndOwnerIdDto.transformList(this.categoryRepository.findAll(authenticatedPerson.getId()));
    }

    // ------------------------- READ ONE -------------------------
    public CategoryReadOneDto readOneCategory(Long categoryId){
        this.authenticationMethods.checkIfCategoryExists(categoryId);
        this.authenticationMethods.checkIfCategoryIsFromAnotherPerson(categoryId);
        Category updatedCategory = this.categoryRepository.findById(categoryId).get();
        return new CategoryReadOneDto(updatedCategory);
    }

    // ------------------------- UPDATE -------------------------
    public List<CategoryWithIdNameAndOwnerIdDto> updateCategory(Long categoryId, CategoryCreateDto categoryDto){
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        this.authenticationMethods.checkIfCategoryExists(categoryId);
        this.authenticationMethods.checkIfCategoryIsFromAnotherPerson(categoryId);
        Category updatedCategory = this.categoryRepository.findById(categoryId).get();
        //attributes validations
        if(categoryDto.getName() != null){
            this.categoryNameValidations.hasThePerfectLength(categoryDto.getName());
            this.categoryNameValidations.personAlreadyHasThisCategoryName(categoryDto.getName(), authenticatedPerson.getId());
            updatedCategory.setName(categoryDto.getName());
        }
        //saved on db
        this.categoryRepository.save(updatedCategory);
        return CategoryWithIdNameAndOwnerIdDto.transformList(this.categoryRepository.findAll(authenticatedPerson.getId()));
    }

    // ------------------------- DELETE -------------------------
    public List<CategoryWithIdNameAndOwnerIdDto> deleteCategoryById(Long categoryId){
        this.authenticationMethods.checkIfCategoryExists(categoryId);
        this.authenticationMethods.checkIfCategoryIsFromAnotherPerson(categoryId);
        this.authenticationMethods.checkIfCategoryContainsTasks(categoryId);
        //delete from db
        this.categoryRepository.deleteById(categoryId);
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        return CategoryWithIdNameAndOwnerIdDto.transformList(this.categoryRepository.findAll(authenticatedPerson.getId()));
    }   
}