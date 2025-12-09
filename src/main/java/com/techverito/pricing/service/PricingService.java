package com.techverito.pricing.service;

import com.techverito.pricing.model.CartItem;
import com.techverito.pricing.model.DiscountType;
import com.techverito.pricing.model.ItemBreakdown;
import com.techverito.pricing.model.PricingResponse;
import com.techverito.pricing.model.ProductCategory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PricingService {

    private static final BigDecimal ELECTRONICS_DISCOUNT = new BigDecimal("0.10");
    private static final BigDecimal GROCERY_DISCOUNT = new BigDecimal("0.05");
    private static final BigDecimal BULK_DISCOUNT = new BigDecimal("0.05");
    private static final BigDecimal CART_VALUE_DISCOUNT = new BigDecimal("0.03");
    private static final BigDecimal CART_VALUE_THRESHOLD = new BigDecimal("10000.00");
    private static final int BULK_QUANTITY_THRESHOLD = 10;

    public PricingResponse calculatePricing(List<CartItem> cartItems) {
        List<ItemBreakdown> itemBreakdowns = new ArrayList<>();
        Set<DiscountType> appliedDiscounts = new HashSet<>();

        BigDecimal cartTotalBeforeDiscount = BigDecimal.ZERO;
        BigDecimal cartTotalAfterItemDiscounts = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal lineTotalBeforeDiscount = item.getLineTotalBeforeDiscount();
            cartTotalBeforeDiscount = cartTotalBeforeDiscount.add(lineTotalBeforeDiscount);

            BigDecimal lineDiscount = BigDecimal.ZERO;
            BigDecimal lineTotalAfterDiscount = lineTotalBeforeDiscount;

            BigDecimal categoryDiscount = applyCategoryDiscount(item, lineTotalBeforeDiscount, appliedDiscounts);
            lineDiscount = lineDiscount.add(categoryDiscount);
            lineTotalAfterDiscount = lineTotalAfterDiscount.subtract(categoryDiscount);

            BigDecimal bulkDiscount = applyBulkDiscount(item, lineTotalAfterDiscount, appliedDiscounts);
            lineDiscount = lineDiscount.add(bulkDiscount);
            lineTotalAfterDiscount = lineTotalAfterDiscount.subtract(bulkDiscount);

            cartTotalAfterItemDiscounts = cartTotalAfterItemDiscounts.add(lineTotalAfterDiscount);

            ItemBreakdown breakdown = new ItemBreakdown(
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                lineTotalBeforeDiscount,
                lineDiscount,
                lineTotalAfterDiscount
            );
            itemBreakdowns.add(breakdown);
        }

        BigDecimal cartLevelDiscount = BigDecimal.ZERO;
        if (cartTotalBeforeDiscount.compareTo(CART_VALUE_THRESHOLD) >= 0) {
            cartLevelDiscount = cartTotalAfterItemDiscounts.multiply(CART_VALUE_DISCOUNT)
                .setScale(2, RoundingMode.HALF_UP);
            appliedDiscounts.add(DiscountType.CART_VALUE);
        }

        BigDecimal cartTotalAfterDiscount = cartTotalAfterItemDiscounts.subtract(cartLevelDiscount);
        BigDecimal totalDiscount = cartTotalBeforeDiscount.subtract(cartTotalAfterDiscount);

        return new PricingResponse(
            itemBreakdowns,
            cartTotalBeforeDiscount,
            totalDiscount,
            cartTotalAfterDiscount,
            new ArrayList<>(appliedDiscounts)
        );
    }

    private BigDecimal applyCategoryDiscount(CartItem item, BigDecimal lineTotal, Set<DiscountType> appliedDiscounts) {
        BigDecimal discount = BigDecimal.ZERO;

        if (item.getCategory() == ProductCategory.ELECTRONICS) {
            discount = lineTotal.multiply(ELECTRONICS_DISCOUNT).setScale(2, RoundingMode.HALF_UP);
            appliedDiscounts.add(DiscountType.CATEGORY_ELECTRONICS);
        } else if (item.getCategory() == ProductCategory.GROCERY) {
            discount = lineTotal.multiply(GROCERY_DISCOUNT).setScale(2, RoundingMode.HALF_UP);
            appliedDiscounts.add(DiscountType.CATEGORY_GROCERY);
        }

        return discount;
    }

    private BigDecimal applyBulkDiscount(CartItem item, BigDecimal lineTotalAfterCategory, Set<DiscountType> appliedDiscounts) {
        BigDecimal discount = BigDecimal.ZERO;

        if (item.getQuantity() >= BULK_QUANTITY_THRESHOLD) {
            discount = lineTotalAfterCategory.multiply(BULK_DISCOUNT).setScale(2, RoundingMode.HALF_UP);
            appliedDiscounts.add(DiscountType.BULK_QUANTITY);
        }

        return discount;
    }
}
