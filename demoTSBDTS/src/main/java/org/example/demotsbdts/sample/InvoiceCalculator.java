package org.example.demotsbdts.sample;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class InvoiceCalculator {
    public BigDecimal calculateSubtotal(List<BigDecimal> prices) {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (BigDecimal price : prices) {
            if (price != null) {
                subtotal = subtotal.add(price);
            }
        }

        return subtotal;
    }

    public BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercent) {
        if (amount == null || discountPercent == null) {
            throw new IllegalArgumentException("Amount and discount cannot be null");
        }

        BigDecimal discount = amount.multiply(discountPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return amount.subtract(discount);
    }

    public BigDecimal calculateTax(BigDecimal amount, BigDecimal taxPercent) {
        if (amount == null || taxPercent == null) {
            throw new IllegalArgumentException("Amount and tax cannot be null");
        }

        return amount.multiply(taxPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal tax, BigDecimal shippingCost) {
        BigDecimal total = BigDecimal.ZERO;

        if (subtotal != null) {
            total = total.add(subtotal);
        }

        if (tax != null) {
            total = total.add(tax);
        }

        if (shippingCost != null) {
            total = total.add(shippingCost);
        }

        return total;
    }

    public boolean isEligibleForFreeShipping(BigDecimal total) {
        if (total == null) {
            return false;
        }

        return total.compareTo(BigDecimal.valueOf(250)) >= 0;
    }
}
