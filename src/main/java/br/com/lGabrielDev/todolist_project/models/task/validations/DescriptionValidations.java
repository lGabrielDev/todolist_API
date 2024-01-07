package br.com.lGabrielDev.todolist_project.models.task.validations;

import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.DescriptionAttributeIsWrongException;

@Component
public class DescriptionValidations {

    // ------------- description length is between 0 and 100 -------------
    public Boolean descriptionHasThePerfectLength(String descriptionToCheck){
        if(descriptionToCheck == null || descriptionToCheck.length() > 100){
            throw new DescriptionAttributeIsWrongException("'description' must have between 0 and 100 characters");
        }
        return true;
    }
}