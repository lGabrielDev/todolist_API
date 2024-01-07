package br.com.lGabrielDev.todolist_project.models.task;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;

@DataJpaTest
public class TaskRepositoryTest {
    
    //injected attributes
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    private Person ownerOfAll;
    private Category workCategory;
    private Task task1;

    @BeforeEach
    public void setUp(){
        this.categoryRepository.deleteAll();
        this.taskRepository.deleteAll();
        this.personRepository.deleteAll();

        this.ownerOfAll = new Person();
        this.ownerOfAll.setUsername("user1");

        this.workCategory = new Category();
        this.workCategory.setName("work");
        this.workCategory.setOwner(ownerOfAll);

        this.task1 = new Task();
        task1.setTitle("task1");
        task1.setCategory(workCategory);
        task1.setOwner(ownerOfAll);

        this.personRepository.save(this.ownerOfAll);
        this.categoryRepository.save(this.workCategory);

        //bilaterally
        this.workCategory.getTasks().add(task1);
    }

    // ------ find all, without any filter ------
    @Test
    @DisplayName("It should return a list of 1 task, without any filter")
    public void itShouldReturnAListOf1TasksWithoutAnyFilter(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();

        this.taskRepository.save(this.task1);
        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("It should return an empty list, without any filter")
    public void itShouldReturnAnEmptyListWithoutAnyFilter(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    // ------ fild all, filtered by 'priority' ------
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority'")
    public void itShouldReturnAListOf1TasksFilteredByPriority(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();

        this.task1.setPriority(2);
        this.taskRepository.save(task1);
        //bilaterally
        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);

        Integer priority = this.task1.getPriority();
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(2);
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'priority'")
    public void itShouldReturnAnEmptyListFilteredByPriority(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        Integer priority = 2;
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    // ------ fild all, filtered by 'completed' ------
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'completed'")
    public void itShouldReturnAListOf1TasksFilteredByCompleted(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
 
        this.task1.setCompleted(true);
        this.taskRepository.save(task1);
        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);

        Boolean isCompleted = task1.getCompleted();
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, isCompleted);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);
        Assertions.assertThat(methodResult.get(0).getCompleted()).isTrue();
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'completed'")
    public void itShouldReturnAnEmptyListFilteredByCompleted(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        Boolean isCompleted = true;
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, isCompleted);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    // ------ find all, filtered by 'title' like ------
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'title' like")
    public void itShouldReturnAListOf1TasksFilteredByTitleLike(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();

        this.task1.setTitle("title something");
        this.taskRepository.save(task1);
        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);

        String titleLike = "something";
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("title something");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);  
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'title' like")
    public void itShouldReturnAnEmptyListFilteredByTitleLike(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        String titleLike = "something";
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, titleLike);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    // ------ find all, filtered by 'priority' AND 'completed' ------
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' AND 'completed'")
    public void itShouldReturnAListOf1TasksFilteredByPriorityAndCompleted(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
    
        this.task1.setPriority(3);
        this.task1.setCompleted(false);
        this.taskRepository.save(task1);

        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);

        Integer priority = task1.getPriority();
        Boolean isCompleted = task1.getCompleted();
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority, isCompleted);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(3);
        Assertions.assertThat(methodResult.get(0).getCompleted()).isFalse();  
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'priority' AND 'completed'")
    public void itShouldReturnAnEmptyListFilteredByPriorityAndCompleted(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        Integer priority = 3;
        Boolean isCompleted = false;
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority, isCompleted);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }

    // ------ find all, filtered by 'priority' AND 'title' like ------
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' AND 'title' like")
    public void itShouldReturnAListOf1TasksFilteredByPriorityAndTitleLike(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
     
        this.task1.setTitle("task something");
        this.task1.setPriority(3);
        this.taskRepository.save(task1);

        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);

        Integer priority = task1.getPriority();
        String titleLike = "something";
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task something");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(3);
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'priority' AND 'title' like")
    public void itShouldReturnAnEmptyListFilteredByPriorityAndTitleLike(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        Integer priority = 3;
        String titleLike = "something";
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority, titleLike);
        //assert
        Assertions.assertThat(methodResult).isEmpty();  
    }

    // ------ find all, filtered by 'priority' AND 'completed' AND 'title' like ------
    @Test
    @DisplayName("It should return a list of 1 task, filtered by 'priority' AND 'completed' AND 'title' like")
    public void itShouldReturnAListOf1TasksFilteredByPriorityAndCompletedAndTitleLike(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        
        this.task1.setPriority(3);
        this.task1.setCompleted(true);
        this.task1.setTitle("task something");
        this.taskRepository.save(task1);

        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);

        Integer priority = 3;
        Boolean isCompleted = true;
        String titleLike = "something"; 
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority, isCompleted, titleLike);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).hasSize(1);
        Assertions.assertThat(methodResult.get(0).getTitle()).isEqualTo("task something");
        Assertions.assertThat(methodResult.get(0).getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.get(0).getOwner().getId()).isEqualTo(ownerId);
        Assertions.assertThat(methodResult.get(0).getPriority()).isEqualTo(3);
        Assertions.assertThat(methodResult.get(0).getCompleted()).isTrue(); 
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'priority' AND 'completed' AND 'title' Like")
    public void itShouldReturnAnEmptyListFilteredByPriorityAndCompletedAndTitleLike(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        Integer priority = 3;
        Boolean isCompleted = true;
        String titleLike = "something";
        //act
        List<Task> methodResult = this.taskRepository.findAll(ownerId, priority, isCompleted, titleLike);
        //assert
        Assertions.assertThat(methodResult).isEmpty();  
    }


    // ------ find by 'title' ------
    @Test
    @DisplayName("It should return a Task, filtered by 'title'")
    public void itShouldReturnATaskFilteredByTitle(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();

        this.task1.setTitle("task1");
        this.taskRepository.save(task1);

        this.ownerOfAll.getTasks().add(task1);
        this.personRepository.save(this.ownerOfAll);
        String titleToFind = "task1";
        //act
        Task methodResult = this.taskRepository.findByTitle(titleToFind, ownerId).get();
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult.getTitle()).isEqualTo("task1");
        Assertions.assertThat(methodResult.getOwner().getUsername()).isEqualTo("user1");
        Assertions.assertThat(methodResult.getOwner().getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("It should return an empty list, filtered by 'title'")
    public void itShouldReturnAnEmptyListFilteredByTitle(){
        //arrange
        Long ownerId = this.ownerOfAll.getId();
        String titleToFind = "something";
        //act
        Optional<Task> methodResult = this.taskRepository.findByTitle(titleToFind, ownerId);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }
}