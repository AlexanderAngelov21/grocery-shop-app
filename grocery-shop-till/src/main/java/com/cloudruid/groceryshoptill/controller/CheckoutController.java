package com.cloudruid.groceryshoptill.controller;

import com.cloudruid.groceryshoptill.dto.CheckoutRequest;
import com.cloudruid.groceryshoptill.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping()
    public ResponseEntity<String> calculateTotal(@RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.calculateTotal(request));
    }
}