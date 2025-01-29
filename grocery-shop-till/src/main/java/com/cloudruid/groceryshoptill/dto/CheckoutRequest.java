package com.cloudruid.groceryshoptill.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CheckoutRequest {
    private Map<String, List<String>> deals;   // Deal type -> List of items
    private Long basketId;                     // Items to scan in the basket
}