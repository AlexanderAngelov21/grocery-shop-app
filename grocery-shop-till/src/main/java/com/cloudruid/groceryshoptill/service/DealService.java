package com.cloudruid.groceryshoptill.service;

import com.cloudruid.groceryshoptill.model.Deal;
import com.cloudruid.groceryshoptill.repository.DealRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DealService {
    private final DealRepository dealRepository;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    public Deal addDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }
    public void deleteDeal(Deal deal) {
        Optional<Deal> existingDeal = dealRepository.findById(deal.getId());
        existingDeal.ifPresent(dealRepository::delete);
    }
}