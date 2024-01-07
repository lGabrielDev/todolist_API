package br.com.lGabrielDev.todolist_project.models.task.validations;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.Task;
import br.com.lGabrielDev.todolist_project.models.task.TaskRepository;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TitleAttributeIsWrongException;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;

@Component
public class TitleValidations {

    //injected attributes
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    AuthenticationMethods authenticationMethods;

    // ------------------- title cannot be null -------------------
    public Boolean titleIsNull(String titleToCheck){
        if(titleToCheck == null){
            throw new TitleAttributeIsWrongException("'title' cannot be null");
        }
        return false;
    }

    // ------------------- title length between 1 and 50 -------------------
    public Boolean titleLengthHasThePerfectSize(String titleToCheck){
        if(titleToCheck.length() < 1 || titleToCheck.length() > 50){
            throw new TitleAttributeIsWrongException("'title' length must have between 1 and 50 characters");
        }
        return true;
    }

    // ------------------- title already exists ------------------
    public Boolean titleAlreadyExists(String titleToCheck){
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        Optional<Task> tOptional = this.taskRepository.findByTitle(titleToCheck, authenticatedPerson.getId());
        if(tOptional.isPresent()){
            Task taskToBeCreated = tOptional.get();

            if(taskToBeCreated.getOwner().getId() == authenticatedPerson.getId()){
                throw new TitleAttributeIsWrongException("You already has a task with this title");
            }
        }
        return false;
    }

    // ------------------- title is fully correct ------------------
    public void titleIsFullyCorrect(String titleToCheck){
        this.titleIsNull(titleToCheck);
        this.titleLengthHasThePerfectSize(titleToCheck);
        this.titleAlreadyExists(titleToCheck);
    }
}