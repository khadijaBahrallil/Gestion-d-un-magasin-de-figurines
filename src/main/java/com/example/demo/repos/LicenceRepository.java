package com.example.demo.repos;

import com.example.demo.entities.Licence;
import org.springframework.data.repository.CrudRepository;


public interface LicenceRepository extends CrudRepository<Licence, Integer> {

    Licence findCategoryById(Integer id);
}

