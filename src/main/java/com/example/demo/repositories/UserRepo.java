package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {


    @Query(" select u from User u " +
            " where u.userName = ?1")
    Optional<User> findUserWithName(String username);
}
