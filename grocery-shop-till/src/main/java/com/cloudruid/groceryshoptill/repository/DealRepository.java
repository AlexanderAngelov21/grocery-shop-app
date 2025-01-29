package com.cloudruid.groceryshoptill.repository;

import com.cloudruid.groceryshoptill.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}