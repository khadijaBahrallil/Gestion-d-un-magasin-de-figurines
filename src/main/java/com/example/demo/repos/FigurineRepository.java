package com.example.demo.repos;

import com.example.demo.entities.Figurine;
import org.springframework.data.repository.CrudRepository;

public interface FigurineRepository extends CrudRepository<Figurine, Integer> {

    Figurine findFigurineById(Integer id);
}
