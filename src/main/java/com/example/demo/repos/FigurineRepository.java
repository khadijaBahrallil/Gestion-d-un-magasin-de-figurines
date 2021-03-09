package com.example.demo.repos;

import com.example.demo.entities.Figurine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FigurineRepository extends CrudRepository<Figurine, Integer> {
    List<Figurine> findAll();
    Figurine findFigurineById(Integer id);
}
