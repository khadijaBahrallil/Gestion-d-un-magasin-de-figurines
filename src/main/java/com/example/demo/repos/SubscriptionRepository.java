package com.example.demo.repos;

import com.example.demo.entities.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    List<Subscription> findAll();
    Subscription findSubscriptionById(Integer id);
}