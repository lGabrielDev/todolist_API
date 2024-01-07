package br.com.lGabrielDev.todolist_project.models.category;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // -------------- READ --------------
    //read all, filtered by person #ID
    @Query(nativeQuery = false, value = "SELECT c FROM Category c WHERE c.owner.id = :ownerId ORDER BY c.id ASC")
    public List<Category> findAll(@Param(value = "ownerId") Long ownerId);


    //find by 'category_name', filtered by owner_id
    @Query(nativeQuery = false, value = "SELECT c FROM Category c WHERE c.name = :categoryName AND c.owner.id = :ownerId")
    public Optional<Category> findByName(@Param(value = "categoryName") String categoryName, @Param(value = "ownerId") Long ownerId);
}