package br.com.lGabrielDev.todolist_project.models.person;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
public class PersonRepositoryTest {
    
    //injected attrubutes
    @Autowired
    private PersonRepository personRepository;

    private Person personToFind;

    @BeforeEach
    public void setUp(){
        this.personToFind = new Person("sonic", "123");
        this.personRepository.save(personToFind);
    }

    // -------------- find by username --------------
    @Test
    @DisplayName("It should return a Person because username is correct")
    public void itShouldReturnAPersonBecauseTheUsernameIsCorrect(){
        //arrange
        String usernameToCheck = "sonic";
        //act
        Person methodResult = this.personRepository.findByUsername(usernameToCheck).get();
        //assert
        Assertions.assertThat(methodResult).isEqualTo(personToFind);
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.getUsername()).isEqualTo(usernameToCheck);
    }

    @Test
    @DisplayName("It should return an exception because there is no person with this username")
    public void itShouldReturnAnExceptionBecauseThereIsNoPersonWithThisUsername(){
        //arrange
        String usernameToCheck = "personThatDoesNotExists";
        //act
        Optional<Person> methodResult = this.personRepository.findByUsername(usernameToCheck);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }
}