package com.techelevator.controller;

import com.techelevator.dao.WishListDao;
import com.techelevator.model.Product;
import com.techelevator.model.WishList;
import com.techelevator.model.WishlistItem;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * WishlistController handles all HTTP requests for retrieving, adding, and deleting wishlists and wishlist items
 */

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/wishlists")
public class WishlistController {

    private WishListDao dao;

    public WishlistController(WishListDao dao) {
        this.dao = dao;
    }

    // gets all wishlists for logged-in user
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<WishList> list(Principal principal) {
        return dao.getAllWishlists(principal);
    }

    // gets specified wishlist for logged-in user and all items in list
    @RequestMapping(path = "/{wishlistId}", method = RequestMethod.GET)
    public WishList getOneWishlist(@PathVariable int wishlistId, Principal principal) {
        WishList wishList = dao.getWishList(wishlistId, principal);
        if (wishList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wishlist does not exist");
        }
        else {
            return wishList;
        }
    }

    // Creates a new wishlist for logged in user and populates with specified products
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public WishList create(@RequestBody WishList wishList, Principal principal) {
        return dao.createWishlist(wishList, principal);
    }

    // Adds an item to specified user wishlist
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{wishlistId}/products/{productId}", method = RequestMethod.POST)
    public WishlistItem addItem(@PathVariable int wishlistId , @PathVariable int productId, Principal principal) {
        return dao.addItem(wishlistId, productId, principal);
    }

    //Deletes a specified wishlist
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{wishlistId}", method = RequestMethod.DELETE)
    public void deleteWishlist(@PathVariable int wishlistId, Principal principal) {
        dao.deleteWishlist(wishlistId, principal);
    }

    // Deletes an item off of a wishlist
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{wishlistId}/products/{productId}", method = RequestMethod.DELETE)
    public void deleteItemFromWishlist(@PathVariable int wishlistId, @PathVariable int productId) {
        dao.removeItem(wishlistId, productId);
    }
}
