package com.cloudruid.groceryshoptill.service;


import com.cloudruid.groceryshoptill.model.Product;
import com.cloudruid.groceryshoptill.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProduct(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public void deleteProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findByNameIgnoreCase(product.getName());
        existingProduct.ifPresent(productRepository::delete);
    }
}
