package com.example.demo.repos;

import com.example.demo.entities.Category;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Category findCategoryById(Integer id);
}

