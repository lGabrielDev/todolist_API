package br.com.lGabrielDev.todolist_project.models.person.validations;

import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.exceptions.PasswordIsWrongException;

@Component
public class PasswordValidations {

    //password cannot be null
    public Boolean passwordIsNull(String passwordToCheck){
        if(passwordToCheck == null){
            return true;
        }
        return false;
    }

    //password contains between 8 and 20 characters
    public Boolean passwordContainsThePerfectLength(String passwordToCheck){
        Integer minCharacters = 8;
        Integer maxCharacters = 20;
        if(passwordToCheck.length() < minCharacters || passwordToCheck.length() > maxCharacters){
            return false;
        }
        return true;
    }

    //password contains white spaces
    public Boolean passwordContainsWhiteSpace(String passwordToCheck){
        Character passwordCharacter;
        for(int i=0; i < passwordToCheck.length(); i++){
            passwordCharacter = passwordToCheck.charAt(i);

            if(Character.isWhitespace(passwordCharacter)){
                return true;
            }
        }
        return false;
    }

    //password contains Uppercase letters
    public Boolean passwordContainsUpperCaseLetter(String passwordToCheck){
        Character passwordCharacter;
        //we going to loop through the password
        for(int i=0; i < passwordToCheck.length(); i++){
            passwordCharacter = passwordToCheck.charAt(i);
            if(Character.isLetter(passwordCharacter)){
                String characterAsString = String.valueOf(passwordCharacter);
                if(characterAsString.equals(characterAsString.toUpperCase())){
                    return true;
                }
            }
        }
        return false;
    }

    //password contains numbers
    public Boolean passwordContainsNumbers(String passwordToCheck){
        Character passwordCharacter;
        for(int i=0; i < passwordToCheck.length(); i++){
            passwordCharacter = passwordToCheck.charAt(i);

            if(Character.isDigit(passwordCharacter)){
                return true;
            }
        }
        return false;
    }

    //password contains 2 special characters
    public Boolean passwordContains2SpecialCharacters(String passwordToCheck){
        Character passwordCharacter;
        Integer specialCharacters = 0;
        for(int i=0; i < passwordToCheck.length(); i++){
            passwordCharacter = passwordToCheck.charAt(i);
            //if the character is not a number AND it's not a letter
            if(!(Character.isDigit(passwordCharacter)) && !(Character.isLetter(passwordCharacter))){
                specialCharacters ++;
                if(specialCharacters >= 2){
                    return true;
                }
            }
        }
        return false;
    }
    
    //check if the password is correct. Here, we going to call all the validations above.
    public Boolean isPasswordFullyCorrect(String passwordToCheck){
        if(this.passwordIsNull(passwordToCheck)){
            throw new PasswordIsWrongException("'Password' cannot be null");
        }
        if(!(this.passwordContainsThePerfectLength(passwordToCheck))){
            throw new PasswordIsWrongException("'Password' must have between 8 and 20 characters");
        }
        if(this.passwordContainsWhiteSpace(passwordToCheck)){
            throw new PasswordIsWrongException("'Password' cannot have white space");
        }
        if(!(this.passwordContainsUpperCaseLetter(passwordToCheck))){
            throw new PasswordIsWrongException("'Password' must have at least 1 UPPERCASE LETTER");
        }
        if(!(this.passwordContainsNumbers(passwordToCheck))){
            throw new PasswordIsWrongException("'Password' must have at least 1 number");
        }
        if(!(this.passwordContains2SpecialCharacters(passwordToCheck))){
            throw new PasswordIsWrongException("'Password' must have 2 special character");
        }
        return true;
    }
}