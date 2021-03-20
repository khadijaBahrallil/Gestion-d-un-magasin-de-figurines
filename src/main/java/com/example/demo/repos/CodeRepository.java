package com.example.demo.repos;

import com.example.demo.entities.Category;
import com.example.demo.entities.PromoCodeClassic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeRepository extends CrudRepository<PromoCodeClassic, Integer> {
    List<PromoCodeClassic> findAll();
    PromoCodeClassic findCodeClassicById(Integer id);
    @Query("SELECT pc FROM PromoCodeClassic pc WHERE pc.code LIKE CONCAT('%',:code,'%')")
    List<PromoCodeClassic> findCodeWithPartOfName(@Param("code") String code);
}
