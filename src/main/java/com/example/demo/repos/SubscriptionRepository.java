package com.example.demo.repos;

import com.example.demo.entities.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    List<Subscription> findAll();
    Subscription findSubscriptionById(Integer id);
    @Query("SELECT s FROM Subscription s WHERE s.name LIKE CONCAT('%',:subscriptionname,'%') OR s.text LIKE CONCAT('%',:subscriptionname,'%')")
    List<Subscription> findSubscriptionWithPartOfName(@Param("subscriptionname") String subscriptionname);
}