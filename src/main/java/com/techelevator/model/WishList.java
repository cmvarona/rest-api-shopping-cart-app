package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Model class for Wishlist database, includes all attributes specified in database and a list of WishListItems
 */

public class WishList {

    private int wishListId;
    private int userId;
    private String name;
    private List<WishlistItem> items;


    public WishList(int wishListId, int userId, String name) {
        this.wishListId = wishListId;
        this.userId = userId;
        this.name = name;
    }
    public WishList() {}

    public int getWishListId() {
        return wishListId;
    }
    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WishlistItem> getItems() {
        return items;
    }

    public void setItems(List<WishlistItem> items) {
        this.items = items;
    }
}
