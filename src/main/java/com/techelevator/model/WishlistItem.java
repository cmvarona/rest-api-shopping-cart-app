package com.techelevator.model;

import java.math.BigDecimal;

/**
 * Model class that represent wishlistItem in database, also includes name and price from product table to be
 * displayed to the user
 */

public class WishlistItem {

    private int wishlistItemId;
    private int wishlistId;
    private int productId;
    private String name;
    private BigDecimal price;

    public WishlistItem(int wishlistItemId, int wishlistId, int productId, String name, BigDecimal price) {
        this.wishlistItemId = wishlistItemId;
        this.wishlistId = wishlistId;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }
    public WishlistItem(){}

    public int getWishlistItemId() {
        return wishlistItemId;
    }

    public void setWishlistItemId(int wishlistItemId) {
        this.wishlistItemId = wishlistItemId;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
