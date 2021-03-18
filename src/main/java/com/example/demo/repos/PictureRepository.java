package com.example.demo.repos;

import com.example.demo.entities.Picture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends CrudRepository<Picture, Integer>{

    List<Picture> findAll();
    Picture findPictureById(Integer id);
    @Query(" select p from Picture p  where p.name = ?1")
    Picture findPictureByName(String name);
}


