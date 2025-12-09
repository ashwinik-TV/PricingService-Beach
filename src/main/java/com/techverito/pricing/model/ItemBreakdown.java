package com.techverito.pricing.model;

import java.math.BigDecimal;

public class ItemBreakdown {
    private String productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotalBeforeDiscount;
    private BigDecimal lineDiscount;
    private BigDecimal lineTotalAfterDiscount;

    public ItemBreakdown(String productId, int quantity, BigDecimal unitPrice,
                         BigDecimal lineTotalBeforeDiscount, BigDecimal lineDiscount,
                         BigDecimal lineTotalAfterDiscount) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotalBeforeDiscount = lineTotalBeforeDiscount;
        this.lineDiscount = lineDiscount;
        this.lineTotalAfterDiscount = lineTotalAfterDiscount;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotalBeforeDiscount() {
        return lineTotalBeforeDiscount;
    }

    public BigDecimal getLineDiscount() {
        return lineDiscount;
    }

    public BigDecimal getLineTotalAfterDiscount() {
        return lineTotalAfterDiscount;
    }
}
