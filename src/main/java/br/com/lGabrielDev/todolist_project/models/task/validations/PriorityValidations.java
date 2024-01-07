package br.com.lGabrielDev.todolist_project.models.task.validations;

import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.PriorityAttributeIsWrongException;

@Component
public class PriorityValidations {
    
    // --------- priority between 1 and 3 ---------
    public Boolean isPriorityValid(Integer priorityToCheck){
        
        if(priorityToCheck == null|| priorityToCheck < 1 || priorityToCheck > 3){
            throw new PriorityAttributeIsWrongException("'priority' must be 1, 2 or 3");
        }
        return true;
    }
}