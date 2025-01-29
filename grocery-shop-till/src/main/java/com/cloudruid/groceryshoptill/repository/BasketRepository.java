package com.cloudruid.groceryshoptill.repository;

import com.cloudruid.groceryshoptill.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}