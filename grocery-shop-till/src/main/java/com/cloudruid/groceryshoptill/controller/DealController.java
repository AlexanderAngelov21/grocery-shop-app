package com.cloudruid.groceryshoptill.controller;

import com.cloudruid.groceryshoptill.model.Deal;
import com.cloudruid.groceryshoptill.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deals")
public class DealController {
    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping
    public ResponseEntity<Deal> addDeal(@RequestBody Deal deal) {
        return ResponseEntity.ok(dealService.addDeal(deal));
    }

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {
        return ResponseEntity.ok(dealService.getAllDeals());
    }
    @DeleteMapping
    public ResponseEntity<String> deleteDeal(@RequestBody Deal deal) {
        dealService.deleteDeal(deal);
        return ResponseEntity.ok("Deal with ID " + deal.getId() + " deleted successfully.");
    }
}