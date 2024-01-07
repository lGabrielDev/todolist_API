package br.com.lGabrielDev.todolist_project.models.task.dto;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.Task;

public class TaskFullDtoTest {

    private Person ownerOfAll;
    private Category workCategory;

    @BeforeEach
    public void setUp(){
        this.ownerOfAll = new Person();
        this.ownerOfAll.setId(1l);
        this.ownerOfAll.setUsername("ownerOfAll");

        this.workCategory = new Category();
        this.workCategory.setId(1l);
    }

    @Test
    @DisplayName("It should convert a list of 'Task' into a list of 'TasksFullDto' successfully")
    public void itShouldReturnAListOfTaskFullDtoWith2TasksOnIt(){
        //arrange
        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("task1");
        task1.setOwner(this.ownerOfAll);
        task1.setCategory(this.workCategory);

        Task task2 = new Task();
        task2.setId(2l);
        task2.setTitle("task2");
        task2.setOwner(this.ownerOfAll);
        task2.setCategory(this.workCategory);
     
        List<Task> listToConvert = List.of(task1, task2);
        //act
        List<TaskFullDto> methodResult = TaskFullDto.convertTaskList(listToConvert);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(2);
        Assertions.assertThat(methodResult.get(0).getId()).isEqualTo(1l);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(1).getId()).isEqualTo(2);
        Assertions.assertThat(methodResult.get(1).getTitle()).isEqualTo("task2");
    }

    @Test
    @DisplayName("It should return an empty list of person")
    public void itShouldReturnAnEmptyListOfPerson(){
        //arrange
        List<Task> listToConvert = new ArrayList<>();
        List<TaskFullDto> methodResult = TaskFullDto.convertTaskList(listToConvert);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }
}
