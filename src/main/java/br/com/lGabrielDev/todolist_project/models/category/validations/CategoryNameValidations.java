package br.com.lGabrielDev.todolist_project.models.category.validations;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNameIsWrong;

@Component
public class CategoryNameValidations {

    //injected attributes
    @Autowired
    private CategoryRepository categoryRepository;
    
    
    // ------------------- 'category name' cannot be null -------------------
    public void categoryNameIsNull(String categoryNameToCheck){
        if(categoryNameToCheck == null){
            throw new CategoryNameIsWrong("'category name' cannot be null");
        }
    }

    // ------------------- 'category name' length between 1 and 20 characters -------------------
    public void hasThePerfectLength(String categoryNameToCheck){
        if(categoryNameToCheck.length() < 1 || categoryNameToCheck.length() > 20 ){
            throw new CategoryNameIsWrong("'category name' must have between 1 and 20 characters");
        }
    }
    
    // ------------------- 'category name' already exists -------------------
    public void personAlreadyHasThisCategoryName(String categoryNameToCheck, Long ownerId){
        Optional<Category> cOptional = this.categoryRepository.findByName(categoryNameToCheck, ownerId);

        if(cOptional.isPresent()){
            throw new CategoryNameIsWrong(String.format("You already has a category named '%s'", categoryNameToCheck));
        }
    }
}