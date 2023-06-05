package com.techelevator.dao;

import com.techelevator.model.CartItem;
import com.techelevator.model.Product;
import com.techelevator.model.WishList;
import com.techelevator.model.WishlistItem;


import java.security.Principal;
import java.util.List;

public interface WishListDao {

    List<WishList> getAllWishlists(Principal principal);
    WishList getWishList(int wishlistId, Principal principal);
    List<WishlistItem> getWishlistItems(Principal principal, int wishListId);
    WishList createWishlist(WishList wishlist, Principal principal);
    WishlistItem addItem(int wishlistId, int productId, Principal principal);
    void deleteWishlist(int wishlistId, Principal principal);
    void removeItem(int wishlistId, int productId);

}
