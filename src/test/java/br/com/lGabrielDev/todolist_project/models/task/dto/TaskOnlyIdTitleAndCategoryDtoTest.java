package br.com.lGabrielDev.todolist_project.models.task.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.task.Task;

public class TaskOnlyIdTitleAndCategoryDtoTest {

    private Category workCategory;

    @BeforeEach
    public void setUp(){
        this.workCategory = new Category();
        this.workCategory.setId(1l);
        this.workCategory.setName("work");
    }
    
    @Test
    @DisplayName("It should convert a list of 'tasks' to a list of 'TaskOnlyIdTitleAndCategoryDto' successfully")
    public void itShouldReturnAListOfTaskOnlyIdTitleAndCategoryDtoWith2TasksOnIt(){
        //arrange
        Task task1 = new Task();
        task1.setId(1l);
        task1.setTitle("work");
        task1.setCategory(this.workCategory);

        Task task2 = new Task();
        task2.setId(2l);
        task2.setTitle("study");
        task2.setCategory(this.workCategory);

        List<Task> listToBeConverted = List.of(task1, task2);
        //act
        List<TaskOnlyIdTitleAndCategoryDto> methodResult = TaskOnlyIdTitleAndCategoryDto.convertList(listToBeConverted);
        //assert
        Assertions.assertThat(methodResult).isNotEmpty();
        Assertions.assertThat(methodResult).hasSize(2);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("work");
        Assertions.assertThat(methodResult.get(1).getTitle()).isEqualTo("study");
    }

    @Test
    @DisplayName("It should convert a list of 'tasks' to a list of 'TaskOnlyIdTitleAndCategoryDto' successfully. But here, the list is going to be an empty list")
    public void itShouldReturnAnEmptyListOfTaskOnlyIdTitleAndCategoryDto(){
        //arrange
        List<Task> listToBeConverted = new ArrayList<>();
        //act
        List<TaskOnlyIdTitleAndCategoryDto> methodResult = TaskOnlyIdTitleAndCategoryDto.convertList(listToBeConverted);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }
}