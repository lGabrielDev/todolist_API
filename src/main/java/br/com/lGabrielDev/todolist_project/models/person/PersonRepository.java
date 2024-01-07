package br.com.lGabrielDev.todolist_project.models.person;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    //find by username
    @Query(nativeQuery = false, value = "SELECT p FROM Person p WHERE p.username = :username")
    public Optional<Person> findByUsername(@Param(value = "username") String username);
}