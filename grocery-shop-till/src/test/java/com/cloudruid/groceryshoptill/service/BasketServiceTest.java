package com.cloudruid.groceryshoptill.service;

import com.cloudruid.groceryshoptill.model.Basket;
import com.cloudruid.groceryshoptill.repository.BasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBasket() {
        Basket basket = new Basket();
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Basket createdBasket = basketService.createBasket();
        assertEquals(basket, createdBasket);
    }

    @Test
    void testScanItems() {
        // Ensure scannedItems is mutable
        Basket existingBasket = new Basket(1L, new ArrayList<>(List.of("apple")));

        // Mock findById to return existingBasket
        when(basketRepository.findById(1L)).thenReturn(Optional.of(existingBasket));

        // Mock save() to return the updated basket
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Basket updatedBasket = basketService.scanItems(1L, List.of("banana", "tomato"));

        // Verify the items were added
        assertEquals(3, updatedBasket.getScannedItems().size()); // apple, banana, tomato
    }
}
