package com.techelevator.dao;

import com.techelevator.model.CartItem;
import com.techelevator.model.CartTotal;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface CartItemDao {

    List<CartItem> list(Principal principal);
    void deleteItem(int cartItemId, Principal principal);
    void deleteCart(Principal principal);
    CartItem add(CartItem item, Principal principal);
    CartItem update(CartItem updatedItem, int userId);
    CartItem insertCartItem(CartItem item, Principal principal);
    BigDecimal getSubtotal(Principal principal);
    BigDecimal getTax(Principal principal);
    BigDecimal getTotal(Principal principal);
    CartTotal getCartTotal(Principal principal);


}
