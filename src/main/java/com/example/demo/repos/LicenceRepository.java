package com.example.demo.repos;

import com.example.demo.entities.Licence;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LicenceRepository extends CrudRepository<Licence, Integer> {
    List<Licence> findAll();
    Licence findLicenceById(Integer id);
    @Query("SELECT l FROM Licence l INNER JOIN l.category c " +
            "WHERE l.name LIKE CONCAT('%',:licencename,'%') " +
            "OR c.name LIKE CONCAT('%',:licencename,'%')")
    List<Licence> findLicenceWithPartOfName(@Param("licencename") String licencename);
}

