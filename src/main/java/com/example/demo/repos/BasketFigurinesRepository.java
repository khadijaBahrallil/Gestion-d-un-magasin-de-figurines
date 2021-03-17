package com.example.demo.repos;

import com.example.demo.entities.Basket;
import com.example.demo.entities.BasketFigurinePK;
import com.example.demo.entities.BasketFigurines;
import com.example.demo.entities.Figurine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketFigurinesRepository extends CrudRepository<BasketFigurines, BasketFigurinePK> {

    @Query(" select bc from BasketFigurines bc  where bc.figurine = ?1 AND bc.basket = ?2")
    BasketFigurines findBasketFigurineByFigurineAndBasket(Figurine figurine, Basket basket);

    @Query(" select bc from BasketFigurines bc  where bc.basket = ?1")
    List<BasketFigurines> findBasketFigurineByBasket(Basket basket);

}
