package com.techelevator.model;

import java.math.BigDecimal;

/**
 *  Class to represent SalesTax returned by TaxRateService
 */

public class SalesTax {

    private BigDecimal salesTax;


    public BigDecimal getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(BigDecimal salesTax) {
        this.salesTax = salesTax;
    }
}
