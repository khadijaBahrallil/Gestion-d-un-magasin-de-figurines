package com.example.demo.repos;

import com.example.demo.entities.Picture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PictureRepository extends CrudRepository<Picture, Integer>{

    List<Picture> findAll();
    Picture findPictureById(Integer id);
}


