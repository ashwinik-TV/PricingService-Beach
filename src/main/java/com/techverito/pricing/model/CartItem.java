package com.techverito.pricing.model;

import java.math.BigDecimal;

public class CartItem {

    private String productId;
    private BigDecimal unitPrice;
    private int quantity;
    private ProductCategory category;

    public CartItem(String productId, BigDecimal unitPrice, int quantity, ProductCategory category) {
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public BigDecimal getLineTotalBeforeDiscount() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
