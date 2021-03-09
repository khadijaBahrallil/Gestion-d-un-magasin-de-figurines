package com.example.demo.repos;

import com.example.demo.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findAll();
    Category findCategoryById(Integer id);
    @Query("SELECT c FROM Category c WHERE c.name LIKE CONCAT('%',:categoryname,'%')")
    List<Category> findCategoryWithPartOfName(@Param("categoryname") String categoryname);
}

