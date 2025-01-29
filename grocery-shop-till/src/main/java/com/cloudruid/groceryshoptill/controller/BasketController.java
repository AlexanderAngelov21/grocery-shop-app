package com.cloudruid.groceryshoptill.controller;
import com.cloudruid.groceryshoptill.model.Basket;
import com.cloudruid.groceryshoptill.service.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/baskets")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }
    @PostMapping
    public ResponseEntity<Basket> createBasket() {
        return ResponseEntity.ok(basketService.createBasket());
    }
    @PostMapping("/{basketId}/scan")
    public ResponseEntity<Basket> scanItems(@PathVariable Long basketId, @RequestBody List<String> items) {
        return ResponseEntity.ok(basketService.scanItems(basketId, items));
    }

    @GetMapping("/{basketId}")
    public ResponseEntity<Basket> getBasket(@PathVariable Long basketId) {
        Optional<Basket> basket = basketService.getBasket(basketId);
        return basket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{basketId}/clear")
    public ResponseEntity<String> clearBasket(@PathVariable Long basketId) {
        basketService.clearBasket(basketId);
        return ResponseEntity.ok("Basket cleared");
    }
}