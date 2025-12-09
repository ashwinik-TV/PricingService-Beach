package com.techverito.pricing.model;

import java.math.BigDecimal;
import java.util.List;

public class PricingResponse {
    private List<ItemBreakdown> items;
    private BigDecimal cartTotalBeforeDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal cartTotalAfterDiscount;
    private List<DiscountType> appliedDiscounts;

    public PricingResponse(List<ItemBreakdown> items, BigDecimal cartTotalBeforeDiscount,
                           BigDecimal totalDiscount, BigDecimal cartTotalAfterDiscount,
                           List<DiscountType> appliedDiscounts) {
        this.items = items;
        this.cartTotalBeforeDiscount = cartTotalBeforeDiscount;
        this.totalDiscount = totalDiscount;
        this.cartTotalAfterDiscount = cartTotalAfterDiscount;
        this.appliedDiscounts = appliedDiscounts;
    }

    public List<ItemBreakdown> getItems() {
        return items;
    }

    public BigDecimal getCartTotalBeforeDiscount() {
        return cartTotalBeforeDiscount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getCartTotalAfterDiscount() {
        return cartTotalAfterDiscount;
    }

    public List<DiscountType> getAppliedDiscounts() {
        return appliedDiscounts;
    }
}
