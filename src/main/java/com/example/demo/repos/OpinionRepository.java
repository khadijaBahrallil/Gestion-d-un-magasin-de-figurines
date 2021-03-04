package com.example.demo.repos;

import com.example.demo.entities.Opinion;
import org.springframework.data.repository.CrudRepository;


public interface OpinionRepository extends CrudRepository<Opinion, Integer> {

    Opinion findOpinionById(Integer id);
}

