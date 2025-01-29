package com.cloudruid.groceryshoptill.service;

import com.cloudruid.groceryshoptill.dto.CheckoutRequest;
import com.cloudruid.groceryshoptill.model.Basket;
import com.cloudruid.groceryshoptill.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

    @Mock
    private ProductService productService;
    @Mock
    private BasketService basketService;

    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateTotalWithDeals() {
        // Mocking products
        when(productService.getProduct("apple")).thenReturn(Optional.of(new Product(1L, "apple", 50)));
        when(productService.getProduct("banana")).thenReturn(Optional.of(new Product(2L, "banana", 40)));
        when(productService.getProduct("tomato")).thenReturn(Optional.of(new Product(3L, "tomato", 30)));
        when(productService.getProduct("potato")).thenReturn(Optional.of(new Product(4L, "potato", 26)));

        // Mock basket
        Basket mockBasket = new Basket(1L, Arrays.asList("apple", "banana", "banana", "potato", "tomato", "banana", "potato"));
        when(basketService.getBasket(1L)).thenReturn(Optional.of(mockBasket));

        // Deals
        Map<String, List<String>> deals = new HashMap<>();
        deals.put("2 for 3", Arrays.asList("apple", "banana", "tomato"));
        deals.put("buy 1 get 1 half price", Arrays.asList("potato"));

        // Checkout request
        CheckoutRequest request = new CheckoutRequest();
        request.setDeals(deals);
        request.setBasketId(1L);

        // Perform checkout
        String result = checkoutService.calculateTotal(request);

        // Verify expected result
        assertEquals("1 aws and 99 clouds", result);
    }

    @Test
    void testCalculateTotalWithoutDeals() {
        // Mocking products
        when(productService.getProduct("apple")).thenReturn(Optional.of(new Product(1L, "apple", 50)));
        when(productService.getProduct("banana")).thenReturn(Optional.of(new Product(2L, "banana", 40)));

        // Mock basket with no deals
        Basket mockBasket = new Basket(2L, Arrays.asList("apple", "banana"));
        when(basketService.getBasket(2L)).thenReturn(Optional.of(mockBasket));

        // Empty deals
        CheckoutRequest request = new CheckoutRequest();
        request.setDeals(new HashMap<>());
        request.setBasketId(2L);

        // Perform checkout
        String result = checkoutService.calculateTotal(request);

        // Verify expected result
        assertEquals("0 aws and 90 clouds", result); // 50c + 40c = 90c
    }
}