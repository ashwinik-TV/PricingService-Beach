package com.techverito.pricing.service;
import com.techverito.pricing.model.CartItem;
import com.techverito.pricing.model.DiscountType;
import com.techverito.pricing.model.ItemBreakdown;
import com.techverito.pricing.model.PricingResponse;
import com.techverito.pricing.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PricingServiceTest {
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @Test
    void shouldCalculateBasicPricing_WhenNoDiscountsApply() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P1", new BigDecimal("100.00"), 2, ProductCategory.OTHER)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);

        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("200.00");
        assertThat(response.getTotalDiscount()).isEqualByComparingTo("0.00");
        assertThat(response.getCartTotalAfterDiscount()).isEqualByComparingTo("200.00");
        assertThat(response.getAppliedDiscounts()).isEmpty();

        ItemBreakdown item = response.getItems().get(0);
        assertThat(item.getProductId()).isEqualTo("P1");
        assertThat(item.getLineTotalBeforeDiscount()).isEqualByComparingTo("200.00");
        assertThat(item.getLineDiscount()).isEqualByComparingTo("0.00");
        assertThat(item.getLineTotalAfterDiscount()).isEqualByComparingTo("200.00");
    }

    @Test
    void shouldApply10PercentDiscount_WhenCategoryIsElectronics() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P1", new BigDecimal("1000.00"), 1, ProductCategory.ELECTRONICS)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);

        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("1000.00");
        assertThat(response.getTotalDiscount()).isEqualByComparingTo("100.00");
        assertThat(response.getCartTotalAfterDiscount()).isEqualByComparingTo("900.00");
        assertThat(response.getAppliedDiscounts()).contains(DiscountType.CATEGORY_ELECTRONICS);

        ItemBreakdown item = response.getItems().get(0);
        assertThat(item.getLineDiscount()).isEqualByComparingTo("100.00");
        assertThat(item.getLineTotalAfterDiscount()).isEqualByComparingTo("900.00");
    }

    @Test
    void shouldApply5PercentDiscount_WhenCategoryIsGrocery() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P2", new BigDecimal("200.00"), 5, ProductCategory.GROCERY)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);

        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("1000.00");
        assertThat(response.getTotalDiscount()).isEqualByComparingTo("50.00");
        assertThat(response.getCartTotalAfterDiscount()).isEqualByComparingTo("950.00");
        assertThat(response.getAppliedDiscounts()).contains(DiscountType.CATEGORY_GROCERY);
    }

    @Test
    void shouldApplyBulkDiscount_WhenQuantityIs10OrMore() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P3", new BigDecimal("100.00"), 12, ProductCategory.ELECTRONICS)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);

        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("1200.00");
        assertThat(response.getTotalDiscount()).isEqualByComparingTo("174.00");
        assertThat(response.getCartTotalAfterDiscount()).isEqualByComparingTo("1026.00");
        assertThat(response.getAppliedDiscounts())
                .contains(DiscountType.CATEGORY_ELECTRONICS, DiscountType.BULK_QUANTITY);
    }

    @Test
    void shouldApplyCartValueDiscount_WhenTotalIsAbove10000() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P4", new BigDecimal("5000.00"), 3, ProductCategory.ELECTRONICS)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);

        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("15000.00");
        assertThat(response.getTotalDiscount()).isEqualByComparingTo("1905.00");
        assertThat(response.getCartTotalAfterDiscount()).isEqualByComparingTo("13095.00");
        assertThat(response.getAppliedDiscounts())
                .contains(DiscountType.CATEGORY_ELECTRONICS, DiscountType.CART_VALUE);
    }

    @Test
    void shouldStackAllDiscounts_WhenAllConditionsMet() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P5", new BigDecimal("1000.00"), 15, ProductCategory.ELECTRONICS)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);

        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("15000.00");
        assertThat(response.getTotalDiscount()).isEqualByComparingTo("2559.75");
        assertThat(response.getCartTotalAfterDiscount()).isEqualByComparingTo("12440.25");
        assertThat(response.getAppliedDiscounts())
                .containsExactlyInAnyOrder(
                        DiscountType.CATEGORY_ELECTRONICS,
                        DiscountType.BULK_QUANTITY,
                        DiscountType.CART_VALUE
                );
    }

    @Test
    void shouldHandleMultipleItems_WithDifferentDiscounts() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem("P6", new BigDecimal("100.00"), 5, ProductCategory.ELECTRONICS),
                new CartItem("P7", new BigDecimal("200.00"), 12, ProductCategory.GROCERY),
                new CartItem("P8", new BigDecimal("50.00"), 2, ProductCategory.ELECTRONICS)
        );

        PricingResponse response = pricingService.calculatePricing(cartItems);


        assertThat(response.getCartTotalBeforeDiscount()).isEqualByComparingTo("3000.00");
        assertThat(response.getItems()).hasSize(3);
        assertThat(response.getAppliedDiscounts())
                .containsExactlyInAnyOrder(
                        DiscountType.CATEGORY_ELECTRONICS,
                        DiscountType.BULK_QUANTITY,
                        DiscountType.CATEGORY_GROCERY
                );

        ItemBreakdown item1 = response.getItems().get(0);
        assertThat(item1.getProductId()).isEqualTo("P6");
        assertThat(item1.getLineTotalBeforeDiscount()).isEqualByComparingTo("500.00");
        assertThat(item1.getLineDiscount()).isEqualByComparingTo("50.00");

        ItemBreakdown item2 = response.getItems().get(1);
        assertThat(item2.getProductId()).isEqualTo("P7");
        assertThat(item2.getLineTotalBeforeDiscount()).isEqualByComparingTo("2400.00");
        assertThat(item2.getLineDiscount()).isEqualByComparingTo("234.00");
    }

}
