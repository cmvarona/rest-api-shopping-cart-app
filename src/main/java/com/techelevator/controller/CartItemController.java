package com.techelevator.controller;

import com.techelevator.dao.CartItemDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.CartItem;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 *  CartItemController class handles all HTTP requests for retrieving, adding, deleting items from user cart.
 */

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/cart")
public class CartItemController {

    private CartItemDao dao;

    public CartItemController(CartItemDao dao) {
        this.dao = dao;
    }

    // Returns list of CartItems for current user and CartTotal
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Object[] list(Principal principal) {
        return new Object[]{dao.list(principal), dao.getCartTotal(principal)};
    }

    // Adds item or updates quantity if item is already in the user cart
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/items", method = RequestMethod.POST)
    public CartItem add(@RequestBody CartItem item, Principal principal) {

        return dao.insertCartItem(item, principal);
    }

    // Deletes an item from current user cart with specified id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/items/{id}", method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable int id, Principal principal) {
        dao.deleteItem(id, principal);
    }

    // Clears entire cart of user
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "", method = RequestMethod.DELETE)
    public void deleteCart(Principal principal) {
        dao.deleteCart(principal);
    }


}
