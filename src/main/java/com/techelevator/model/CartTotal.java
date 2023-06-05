package com.techelevator.model;

import java.math.BigDecimal;

/**
 * Model class that represents the logged-in user's CartTotal
 */
public class CartTotal {

    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal Total;


    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal total) {
        Total = total;
    }
}
