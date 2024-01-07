package br.com.lGabrielDev.todolist_project.models.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
 
    //find by role
    @Query(nativeQuery = false, value = "SELECT r FROM Role r WHERE r.role = :role")
    public Optional <Role> findByRole(@Param(value = "role") RoleEnum role);
}