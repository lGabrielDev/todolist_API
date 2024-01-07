package br.com.lGabrielDev.todolist_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import br.com.lGabrielDev.todolist_project.models.category.Category;
import br.com.lGabrielDev.todolist_project.models.category.CategoryRepository;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryContainsTasksException;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryIsFromAnotherPersonException;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNotFoundException;
import br.com.lGabrielDev.todolist_project.models.person.Person;
import br.com.lGabrielDev.todolist_project.models.task.Task;
import br.com.lGabrielDev.todolist_project.models.task.TaskRepository;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskIsFromAnotherPersonException;
import br.com.lGabrielDev.todolist_project.models.task.exceptions.TaskNotFoundException;

@Component
public class AuthenticationMethods {

    //injected attributes
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ------------------- get the infos from authenticated person -------------------
    public Person getTheAuthenticatedPerson(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //authenticated person
        Person authenticatedPerson = (Person) authentication.getPrincipal();
        return authenticatedPerson;
    }

    // ------------------- Task validations  -------------------
    // ---- Task exists ----
    public void checkIfTaskExists(Long taskId){
        this.taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(String.format("Task #%d doesn't exists", taskId)));
    }

    // ---- A person can only update his own tasks ----
    public void checkIfTaskIsFromAnotherPerson(Long taskId){
        Task taskToFind = this.taskRepository.findById(taskId).get();
        Person authenticatedPerson = this.getTheAuthenticatedPerson();
        if(taskToFind.getOwner().getId() != authenticatedPerson.getId()){
            throw new TaskIsFromAnotherPersonException("You cannot access a task that belongs to another person");
        }
    }

    // ------------------- Category validations  -------------------
    // ---- 'Category' exists ----
    public void checkIfCategoryExists(Long categoryId){
        this.categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(String.format("Category #%d doesn't exists", categoryId)));
    }

    // ---- A person can only update his own categories ----
    public void checkIfCategoryIsFromAnotherPerson(Long categoryId){
        Person authenticatedPerson = this.getTheAuthenticatedPerson();
        Category categoryToFind = this.categoryRepository.findById(categoryId).get();
        if(categoryToFind.getOwner().getId() != authenticatedPerson.getId()){
            throw new CategoryIsFromAnotherPersonException("You cannot access a category that belongs to another person");
        }
    }

    // ---- Category contains tasks on it ----
    public void checkIfCategoryContainsTasks(Long categoryId){
        Category categoryToCheck = this.categoryRepository.findById(categoryId).get();
        if(categoryToCheck.getTasks().size() > 0){
            throw new CategoryContainsTasksException(String.format("You cannot delete category #%d because contains tasks on it. First, you need to delete all tasks from this category", categoryId));
        }
    }
}