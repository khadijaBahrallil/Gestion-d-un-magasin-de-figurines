package com.example.demo.repos;

import com.example.demo.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findAll();
    Category findCategoryById(Integer id);
}

