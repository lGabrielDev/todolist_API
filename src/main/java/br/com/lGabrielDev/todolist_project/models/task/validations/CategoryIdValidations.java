package br.com.lGabrielDev.todolist_project.models.task.validations;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.CategoryAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;

@Component
public class CategoryIdValidations {
    
    //injected attributes
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthenticationMethods authenticationMethods;

    // ---------------- category_id cannot be null ----------------
    public void categoryIdIsNull(Long categoryIdToCheck){
        if(categoryIdToCheck == null){
            throw new CategoryAttributeIsWrongException("'categoryId' cannot be null");
        }
    }

    // ---------------- category exists ----------------
    public void categoryDoesNotExistsShowAllAvailableCategories(Long categoryIdToCheck){
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        if(!(this.categoryRepository.existsById(categoryIdToCheck))){
            //catch all categories from authenticated person
            List<Category> categories = this.categoryRepository.findAll(authenticatedPerson.getId());
            String categoriesToString = "";
            for(int i=0; i<categories.size(); i++){
                if(i == categories.size() -1){ //if it is the last item
                    categoriesToString += String.format("category_id: %d (%s)", categories.get(i).getId(),categories.get(i).getName());
                }
                else{
                    categoriesToString += String.format("category_id: %d (%s),  ",categories.get(i).getId(),categories.get(i).getName());
                }
            }
            throw new CategoryAttributeIsWrongException(String.format("category_id #%d doesn't exists. Available categories: [ %s ]", categoryIdToCheck, categoriesToString));
        }
    }

    // ---------------- category_id if fully correct ----------------
    public void categoryIdIsFullyCorrect(Long categoryId){
        this.categoryIdIsNull(categoryId);
        this.categoryDoesNotExistsShowAllAvailableCategories(categoryId);
        this.authenticationMethods.checkIfCategoryIsFromAnotherPerson(categoryId);
    }
}