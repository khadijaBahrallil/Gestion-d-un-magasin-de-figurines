package com.example.demo.repos;

import com.example.demo.entities.Figurine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FigurineRepository extends CrudRepository<Figurine, Integer> {
    List<Figurine> findAll();
    Figurine findFigurineById(Integer id);
    @Query("SELECT f FROM Figurine f WHERE f.name LIKE CONCAT('%',:figurinename,'%')")
    List<Figurine> findFigurineWithPartOfName(@Param("figurinename") String figurinename);
    @Query("SELECT f FROM Figurine f INNER JOIN f.licence l WHERE l.name LIKE CONCAT('%',:licencename,'%')")
    List<Figurine> findFigurineWithLicence(@Param("licencename") String licencename);
    @Query("SELECT f FROM Figurine f INNER JOIN f.licence l INNER JOIN l.category c WHERE c.name LIKE CONCAT('%',:categoryname,'%')")
    List<Figurine> findFigurineWithCategory(@Param("categoryname") String categoryname);
}
