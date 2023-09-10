package com.trendyol.shipment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basket {
    private List<Product> products;
    private static final int MIN_SAME_SIZE_FOR_UPGRADE = 3;
    private static final Map<ShipmentSize, ShipmentSize> SIZE_MAPPING = new HashMap<>();

    static {
        SIZE_MAPPING.put(ShipmentSize.SMALL, ShipmentSize.MEDIUM);
        SIZE_MAPPING.put(ShipmentSize.MEDIUM, ShipmentSize.LARGE);
        SIZE_MAPPING.put(ShipmentSize.LARGE, ShipmentSize.X_LARGE);
    }

    public ShipmentSize getShipmentSize() {
        // Check for null products or empty list
        if (products == null || products.isEmpty()) {
            throw new IllegalStateException("Basket cannot be empty or null.");
        }

        // Check for null values in products and calculate size counts
        Map<ShipmentSize, Integer> sizeCounts = new HashMap<>();
        for (Product product : products) {
            if (product == null || product.getSize() == null) {
                throw new IllegalStateException("Invalid product or shipment size.");
            }

            ShipmentSize size = product.getSize();
            sizeCounts.put(size, sizeCounts.getOrDefault(size, 0) + 1);
            //If there is no mapping, it defaults to X_LARGE
            if (sizeCounts.get(size) >= MIN_SAME_SIZE_FOR_UPGRADE) {
                return SIZE_MAPPING.getOrDefault(size, ShipmentSize.X_LARGE);
            }
        }

        // If there is no upgrade needed, choose the largest size
        return getLargestSize();
    }

    private ShipmentSize getLargestSize() {
        ShipmentSize largestSize = ShipmentSize.SMALL;

        for (Product product : products) {
            if (product == null || product.getSize() == null) {
                throw new IllegalStateException("Invalid product or shipment size.");
            }
            //If the size of the current product is larger, update largestSize.
            if (product.getSize().compareTo(largestSize) > 0) {
                largestSize = product.getSize();
            }
        }

        return largestSize;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

