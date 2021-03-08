package com.example.demo.repos;

import com.example.demo.entities.Licence;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LicenceRepository extends CrudRepository<Licence, Integer> {
    List<Licence> findAll();
    Licence findLicenceById(Integer id);
}

