package com.techverito.pricing.model;

public enum DiscountType {
    CATEGORY_ELECTRONICS("Category Discount - ELECTRONICS"),
    CATEGORY_GROCERY("Category Discount - GROCERY"),
    BULK_QUANTITY("Bulk Quantity Discount"),
    CART_VALUE("Cart Value Discount");

    private final String description;

    DiscountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
