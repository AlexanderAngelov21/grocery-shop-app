package com.cloudruid.groceryshoptill.service;

import com.cloudruid.groceryshoptill.dto.CheckoutRequest;
import com.cloudruid.groceryshoptill.model.Basket;
import com.cloudruid.groceryshoptill.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckoutService {
    private final ProductService productService;
    private final BasketService basketService;

    public CheckoutService(ProductService productService, BasketService basketService) {
        this.productService = productService;
        this.basketService = basketService;
    }

    public String calculateTotal(CheckoutRequest request) {
        Basket basket = basketService.getBasket(request.getBasketId()).orElse(null);
        if (basket == null) {
            return "Basket not found";
        }

        List<String> scannedItems = basket.getScannedItems();
        Map<String, Integer> itemCounts = new LinkedHashMap<>();
        int totalCost = 0;

        for (String item : scannedItems) {
            itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
        }

        Map<String, List<String>> deals = request.getDeals();
        List<String> twoForThreeItems = deals.getOrDefault("2 for 3", new ArrayList<>());
        List<String> buyOneGetOneHalfPriceItems = deals.getOrDefault("buy 1 get 1 half price", new ArrayList<>());

        // Apply "2 for 3" once to the first 3 matching items
        List<Product> eligibleForTwoForThree = new ArrayList<>();
        for (String item : scannedItems) {
            if (twoForThreeItems.contains(item)) {
                productService.getProduct(item).ifPresent(eligibleForTwoForThree::add);
            }
            if (eligibleForTwoForThree.size() == 3) break; // Stop after first 3 found
        }

        if (eligibleForTwoForThree.size() == 3) {
            eligibleForTwoForThree.sort(Comparator.comparingInt(Product::getPriceInClouds)); // Sort cheapest first
            int cheapestItemPrice = eligibleForTwoForThree.get(0).getPriceInClouds(); // Get cheapest price

            totalCost += eligibleForTwoForThree.get(1).getPriceInClouds();
            totalCost += eligibleForTwoForThree.get(2).getPriceInClouds();

            // Remove these 3 items from itemCounts so they don’t get counted again
            itemCounts.put(eligibleForTwoForThree.get(0).getName(), itemCounts.get(eligibleForTwoForThree.get(0).getName()) - 1);
            itemCounts.put(eligibleForTwoForThree.get(1).getName(), itemCounts.get(eligibleForTwoForThree.get(1).getName()) - 1);
            itemCounts.put(eligibleForTwoForThree.get(2).getName(), itemCounts.get(eligibleForTwoForThree.get(2).getName()) - 1);

            // Remove any items that now have count 0
            itemCounts.entrySet().removeIf(entry -> entry.getValue() <= 0);
        }


        for (String item : buyOneGetOneHalfPriceItems) {
            if (itemCounts.containsKey(item)) {
                int quantity = itemCounts.get(item);
                int price = productService.getProduct(item).map(Product::getPriceInClouds).orElse(0);

                int pairs = quantity / 2;
                int remainingItems = quantity % 2;

                int dealCost = (pairs * 3 * price / 2) + (remainingItems * price);
                totalCost += dealCost;

                itemCounts.remove(item);
            }
        }


        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            int price = productService.getProduct(entry.getKey()).map(Product::getPriceInClouds).orElse(0);
            totalCost += entry.getValue() * price;
        }

        basketService.clearBasket(request.getBasketId());
        return formatPrice(totalCost);
    }

    private String formatPrice(int totalClouds) {
        int aws = totalClouds / 100;
        int clouds = totalClouds % 100;
        return aws + " aws and " + clouds + " clouds";
    }
}
