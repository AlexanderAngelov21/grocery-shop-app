package com.cloudruid.groceryshoptill.service;

import com.cloudruid.groceryshoptill.model.Basket;
import com.cloudruid.groceryshoptill.repository.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }
    public Basket createBasket() {
        Basket newBasket = new Basket();
        return basketRepository.save(newBasket);
    }
    public Basket scanItems(Long basketId, List<String> items) {
        Basket basket = basketRepository.findById(basketId)
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    Basket savedBasket = basketRepository.save(newBasket);
                    return savedBasket;
                });

        basket.getScannedItems().addAll(items);
        return basketRepository.save(basket);
    }

    public Optional<Basket> getBasket(Long basketId) {
        return basketRepository.findById(basketId);
    }

    public void clearBasket(Long basketId) {
        basketRepository.findById(basketId).ifPresent(basket -> {
            basket.getScannedItems().clear();
            basketRepository.save(basket);
        });
    }
}