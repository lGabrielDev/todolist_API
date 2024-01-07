package br.com.lGabrielDev.todolist_project.models.person.validations;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.exceptions.UsernameIsWrong;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;

@Component
public class UsernameValidations {

    //injected attributes
    @Autowired
    private PersonRepository personRepository;
    
    //name cannot be null
    public Boolean usernameIsNull(String usernameToCheck){
        if(usernameToCheck == null){
            return true;
        }
        return false;
    }

    //name cannot have white space
    public Boolean usernameContainsWhiteSpace(String usernameToCheck){
        Character usernameCharacter;

        for(int i=0; i<usernameToCheck.length(); i++){
            usernameCharacter = usernameToCheck.charAt(i);

            if(Character.isWhitespace(usernameCharacter)){
                return true;
            }
        }
        return false;
    }

    //username must have between 5 and 20 characters
    public Boolean usernameContainsThePerfectLength(String usernameToCheck){
        if(usernameToCheck.length() >= 5 && usernameToCheck.length() <=20){
            return true;
        }
        return false;
    }

    //name already exists
    public Boolean usernameAlreadyExists(String usernameToCheck){
        Optional<Person> pOptional = this.personRepository.findByUsername(usernameToCheck);

        if(pOptional.isPresent()){
            return true;
        }
        return false;
    }

    //check if the username passed through all the validations above
    public Boolean isUsernameFullyCorrect(String usernameToCheck){
        if(this.usernameIsNull(usernameToCheck)){
            throw new UsernameIsWrong("'username' cannot be null");
        }
        if(this.usernameContainsWhiteSpace(usernameToCheck)){
            throw new UsernameIsWrong("'username' cannot have white spaces");
        }
        if(!(this.usernameContainsThePerfectLength(usernameToCheck))){
            throw new UsernameIsWrong("'username' must have between 5 and 20 characters");
        }
        if(this.usernameAlreadyExists(usernameToCheck)){
            throw new UsernameIsWrong("'username' is not unique. Already exists a person with this 'username'");
        }
        return true;
    }
}