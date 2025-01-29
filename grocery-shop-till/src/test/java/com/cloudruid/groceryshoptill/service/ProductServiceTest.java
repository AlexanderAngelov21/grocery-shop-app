package com.cloudruid.groceryshoptill.service;

import com.cloudruid.groceryshoptill.model.Product;
import com.cloudruid.groceryshoptill.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        Product product = new Product(1L, "apple", 50);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(product);
        assertEquals("apple", savedProduct.getName());
    }

    @Test
    void testGetProductByName() {
        Product product = new Product(1L, "banana", 40);
        when(productRepository.findByNameIgnoreCase("banana")).thenReturn(Optional.of(product));

        Optional<Product> retrievedProduct = productService.getProduct("banana");
        assertEquals(40, retrievedProduct.get().getPriceInClouds());
    }
}