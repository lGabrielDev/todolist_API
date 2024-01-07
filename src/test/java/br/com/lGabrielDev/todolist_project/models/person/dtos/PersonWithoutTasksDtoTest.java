package br.com.lGabrielDev.todolist_project.models.person.dtos;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; 
import br.com.lGabrielDev.todolist_project.models.person.Person;

public class PersonWithoutTasksDtoTest {

    @Test
    @DisplayName("It should catch all the persons from the db list and convert into a list of 'PersonWithoutTasksDto' successfully")
    public void itShouldConvertAListOfPersonsFromDbSuccessfully(){
        //arrange
        Person p1 = new Person();
        p1.setUsername("sonic");
        Person p2 = new Person();
        p2.setUsername("sonic");

        List<Person> dataBasePersonList = List.of(p1,p2);
        
        PersonWithoutTasksDto perfectPerson1 = new PersonWithoutTasksDto(p1);
        PersonWithoutTasksDto perfectPerson2 = new PersonWithoutTasksDto(p2);
        List<PersonWithoutTasksDto> expectedListResult = List.of(perfectPerson1, perfectPerson2);

        //act
        List<PersonWithoutTasksDto> methodResult = perfectPerson1.transformList(dataBasePersonList);
        //assert
        Assertions.assertThat(methodResult.size()).isEqualTo(2);
        Assertions.assertThat(methodResult.get(0).getUsername()).isEqualTo(expectedListResult.get(0).getUsername());
        Assertions.assertThat(methodResult.get(0).getCreated_at()).isEqualTo(expectedListResult.get(0).getCreated_at());
        Assertions.assertThat(methodResult.get(1).getUsername()).isEqualTo(expectedListResult.get(1).getUsername());
        Assertions.assertThat(methodResult.get(1).getCreated_at()).isEqualTo(expectedListResult.get(1).getCreated_at());
    }
}