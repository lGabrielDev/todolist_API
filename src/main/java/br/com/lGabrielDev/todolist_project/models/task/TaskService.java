package br.com.lGabrielDev.todolist_project.models.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.person.PersonRepository;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskCreateDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskFullDto;
import br.com.lGabrielDev.todolist_project.models.task.dto.TaskUpdateDto;
import br.com.lGabrielDev.todolist_project.models.task.validations.CategoryIdValidations;
import br.com.lGabrielDev.todolist_project.models.task.validations.DescriptionValidations;
import br.com.lGabrielDev.todolist_project.models.task.validations.PriorityValidations;
import br.com.lGabrielDev.todolist_project.models.task.validations.TitleValidations;
import br.com.lGabrielDev.todolist_project.security.AuthenticationMethods;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    //injected attributes
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PersonRepository personRepository;

    //validation attributes
    @Autowired
    private TitleValidations titleValidations;

    @Autowired
    private DescriptionValidations descriptionValidations;

    @Autowired
    private PriorityValidations priorityValidations;

    @Autowired
    private CategoryIdValidations categoryIdValidations;

    @Autowired
    private AuthenticationMethods authenticationMethods;

    @Autowired
    private CategoryRepository categoryRepository;

    // ------------------------- CREATE -------------------------
    public List<TaskFullDto> createTask(TaskCreateDto taskDto){
        //validations
        this.titleValidations.titleIsFullyCorrect(taskDto.getTitle());
        this.descriptionValidations.descriptionHasThePerfectLength(taskDto.getDescription());
        this.priorityValidations.isPriorityValid(taskDto.getPriority());
        this.categoryIdValidations.categoryIdIsFullyCorrect(taskDto.getCategoryId());
        Task taskToBeCreated = new Task(taskDto);
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        Category taskCategory = this.categoryRepository.findById(taskDto.getCategoryId()).get();
        //bilaterally
        taskToBeCreated.setOwner(authenticatedPerson); //task receives a person
        taskToBeCreated.setCategory(taskCategory); //task receives a category
        authenticatedPerson.getTasks().add(taskToBeCreated); //person receives a new task
        taskCategory.getTasks().add(taskToBeCreated); //category receives a new task
        //save on db
        this.taskRepository.save(taskToBeCreated);
        this.personRepository.save(authenticatedPerson);
        this.categoryRepository.save(taskCategory);
        return TaskFullDto.convertTaskList(this.taskRepository.findAll(authenticatedPerson.getId()));
    }

    // ------------------------- READ ALL -------------------------
    public List<TaskFullDto> readAllTasks(Integer priority, Boolean completed, String titleLike){
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        List<Task> taskList = new ArrayList<>();
        if(priority == null && completed == null && titleLike == null){
            taskList = this.taskRepository.findAll(authenticatedPerson.getId());
        }
        else if(priority != null && completed == null && titleLike == null){
            taskList = this.taskRepository.findAll(authenticatedPerson.getId(), priority);
        }
        else if(priority == null && completed != null && titleLike == null){
            taskList = this.taskRepository.findAll(authenticatedPerson.getId(), completed);
        }
        else if(priority == null && completed == null && titleLike != null){
            taskList = this.taskRepository.findAll(authenticatedPerson.getId(), titleLike);
        }
        else if(priority != null && completed != null && titleLike == null){
            taskList = this.taskRepository.findAll(authenticatedPerson.getId(), priority, completed);
        }
        else if(priority != null && completed == null && titleLike != null){
            taskList = this.taskRepository.findAll(authenticatedPerson.getId(), priority, titleLike);
        }
        else{
            taskList = this.taskRepository.findAll(authenticatedPerson.getId(), priority, completed, titleLike);
        }
        return TaskFullDto.convertTaskList(taskList);
    }

    // ------------------------- UPDATE -------------------------
    public List<TaskFullDto> updateTask(Long taskId, TaskUpdateDto taskDto){
        //first, we check if task exists and it belongs to the authenticated person
        this.authenticationMethods.checkIfTaskExists(taskId);
        this.authenticationMethods.checkIfTaskIsFromAnotherPerson(taskId);
        Task updatedTask = this.taskRepository.findById(taskId).get();
        Person authenticatedPerson = this.authenticationMethods.getTheAuthenticatedPerson();
        //'title' validations
        if(taskDto.getTitle() != null){
            this.titleValidations.titleIsFullyCorrect(taskDto.getTitle());
            updatedTask.setTitle(taskDto.getTitle());
        }
        //'description' validations
        if(taskDto.getDescription() != null){
            this.descriptionValidations.descriptionHasThePerfectLength(taskDto.getDescription());
            updatedTask.setDescription(taskDto.getDescription());
        }
        //'priority' validations
        if(taskDto.getPriority() != null){
            this.priorityValidations.isPriorityValid(taskDto.getPriority());
            updatedTask.setPriority(taskDto.getPriority());
        }
        //'completed' validations
        if(taskDto.getCompleted() != null){
            updatedTask.setCompleted(taskDto.getCompleted());
        }
        //'category_id' validations
        if(taskDto.getCategoryId() != null){
            this.categoryIdValidations.categoryIdIsFullyCorrect(taskDto.getCategoryId());
            if(taskDto.getCategoryId() != updatedTask.getCategory().getId()){
                Category updatedCategory = this.categoryRepository.findById(taskDto.getCategoryId()).get();
                updatedTask.setCategory(updatedCategory);
                //bilaterally
                this.categoryRepository.save(updatedCategory);
            }
        }
        updatedTask.setUpdatedAt(LocalDateTime.now());
        //save the changes on db
        this.taskRepository.save(updatedTask);
        return TaskFullDto.convertTaskList(this.taskRepository.findAll(authenticatedPerson.getId()));
    }

    // ------------------------- DELETE -------------------------
    public List<TaskFullDto> deleteTaskById(Long taskId){
        this.authenticationMethods.checkIfTaskExists(taskId);
        this.authenticationMethods.checkIfTaskIsFromAnotherPerson(taskId);
        this.taskRepository.deleteById(taskId);
        return TaskFullDto.convertTaskList(this.taskRepository.findAll(this.authenticationMethods.getTheAuthenticatedPerson().getId()));
    }
}