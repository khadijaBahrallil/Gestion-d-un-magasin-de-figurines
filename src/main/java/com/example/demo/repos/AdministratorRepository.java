package com.example.demo.repos;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {
    List<Administrator> findAll();
    @Query(" select a from Administrator a  where a.userName = ?1")
    Optional<Administrator> findAdministratorByName(String username);
}
