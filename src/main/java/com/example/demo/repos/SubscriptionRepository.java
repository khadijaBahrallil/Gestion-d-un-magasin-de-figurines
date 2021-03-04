package com.example.demo.repos;

import com.example.demo.entities.Subscription;
import org.springframework.data.repository.CrudRepository;


public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    Subscription findOpinionById(Integer id);
}