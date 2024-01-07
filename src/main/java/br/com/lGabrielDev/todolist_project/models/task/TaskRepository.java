package br.com.lGabrielDev.todolist_project.models.task;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    //find all, without any filter
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId);

    //find all, filtered by 'priority'
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId AND t.priority = :priority ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId, @Param(value = "priority") Integer priority);

    //find all, filtered by 'completed'
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId AND t.completed = :completed ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId, @Param(value = "completed") Boolean completed);

    //find all, filtered by 'title' like
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId AND t.title LIKE CONCAT('%', :titleLike, '%') ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId, @Param(value = "titleLike") String titleLike);
    
    //find all, filtered by 'priority' AND 'completed'
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId AND t.priority = :priority AND t.completed = :completed ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId, @Param(value = "priority") Integer priority, @Param(value = "completed") Boolean completed);

    //find all, filtered by 'priority' AND 'title' like
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId AND t.priority = :priority AND t.title LIKE CONCAT('%' ,:titleLike, '%')  ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId, @Param(value = "priority") Integer priority, @Param(value = "titleLike") String titleLike);

    //find all, filtered by 'priority' AND 'completed' AND 'title' Like
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE p.id = :ownerId AND t.priority = :priority AND t.completed = :completed AND t.title LIKE CONCAT('%' ,:titleLike, '%')  ORDER BY t.priority ASC, t.id DESC")
    public List<Task> findAll(@Param(value = "ownerId") Long ownerId, @Param(value = "priority") Integer priority, @Param(value = "completed") Boolean completed, @Param(value = "titleLike") String titleLike);


    //find by 'title'
    @Query(nativeQuery = false, value = "SELECT t FROM Task t JOIN t.owner p WHERE t.title = :title AND p.id = :ownerId")
    public Optional<Task> findByTitle(@Param(value = "title") String title, @Param(value = "ownerId") Long ownerId);
}