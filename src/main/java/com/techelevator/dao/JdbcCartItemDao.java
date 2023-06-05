package com.techelevator.dao;

import com.techelevator.Service.TaxRateService;
import com.techelevator.model.CartItem;
import com.techelevator.model.CartTotal;
import com.techelevator.model.SalesTax;
import com.techelevator.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * JdbcCartItemDao interacts with the database relating to cartItems and contains methods that implement logic
 * to return cart subtotal, tax, and total.
 */

@Component
public class JdbcCartItemDao implements CartItemDao{

    private final JdbcTemplate jdbc;
    private final UserDao dao;
    private TaxRateService txs;

    public JdbcCartItemDao(JdbcTemplate jdbc, UserDao dao, TaxRateService txs) {
        this.jdbc = jdbc;
        this.dao = dao;
        this.txs = txs;
    }

    // Returns a list of cart items for the current logged in user
    @Override
    public List<CartItem> list(Principal principal) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT cart_item_id, user_id, c.product_id, quantity, price, name FROM cart_item c" +
                " JOIN product p ON p.product_id = c.product_id " +
                "WHERE user_id = ? ";
        SqlRowSet results = jdbc.queryForRowSet(sql, getUserId(principal));
        while (results.next()) {
            CartItem item = mapRowToCartItem(results);
            items.add(item);
        }
        return items;
    }

    // Adds a new item to current user cart
    @Override
    public CartItem add(CartItem item, Principal principal) {
        String addSql = "INSERT INTO cart_item (user_id, product_id, quantity) " +
                "VALUES (?, ?, ?) " +
                " RETURNING cart_item_id";
        Integer newId = jdbc.queryForObject(addSql, Integer.class, getUserId(principal), item.getProductId(), item.getQuantity());
        return getCartItem(newId);
    }

    // Updates the quantity of item added to current user cart
    @Override
    public CartItem update(CartItem updatedItem, int userId) {
        String sql = "UPDATE cart_item " +
                "SET quantity = quantity + ? " +
                "WHERE user_id = ? AND product_id = ?";
        jdbc.update(sql, updatedItem.getQuantity(), userId, updatedItem.getProductId());
        return getCartItem(updatedItem.getCartItemId());
    }

    // Inserts item into user cart; If item is already in the cart, it updates the quantity. If the item is not already
    // in the cart, it adds the item
    @Override
    public CartItem insertCartItem(CartItem item, Principal principal) {
        List<CartItem> items = list(principal);
        for (CartItem cartItem : items) {
            if (item.getProductId() == cartItem.getProductId()) {
                return update(item, getUserId(principal));
            }
        }
        return add(item, principal);
    }

    // Deletes a specified item from user cart
    @Override
    public void deleteItem(int cartItemId, Principal principal) {
        String sql = "DELETE FROM cart_item " +
                "WHERE cart_item_id = ? " +
                "AND user_id = ?";
        jdbc.update(sql, cartItemId, getUserId(principal));
    }

    // Clears the entire cart of the logged-in user
    @Override
    public void deleteCart(Principal principal) {
        String sql = "DELETE FROM cart_item " +
                "WHERE user_id = ?";
        jdbc.update(sql, getUserId(principal));
    }

    // Returns the subtotal of all items in the logged-in user's cart
    @Override
    public BigDecimal getSubtotal(Principal principal) {
        BigDecimal subtotal = new BigDecimal("0.00");
        List<CartItem> items = list(principal);
        for (int i = 0; i < items.size(); i++) {
            BigDecimal itemPrice = items.get(i).getPrice();
            int quantity = items.get(i).getQuantity();
            BigDecimal itemTotal = itemPrice.multiply(BigDecimal.valueOf(quantity));
            subtotal = itemTotal.add(subtotal);
        }
        return subtotal;
    }

    // Calls on TaxRateService to retrieve the tax percentage for the logged-in user's state.
    // Returns the total amount due in tax based on user's subtotal
    @Override
    public BigDecimal getTax(Principal principal) {
        User user = getUser(principal);
        String stateCode = user.getStateCode();
        SalesTax salesTax = txs.getTaxRate(stateCode);
        BigDecimal taxPercent = salesTax.getSalesTax();
        BigDecimal taxDecimal = taxPercent.divide(BigDecimal.valueOf(100.00));
        return taxDecimal.multiply(getSubtotal(principal)).setScale(2, RoundingMode.HALF_UP);
    }

    // Returns total cost including tax for logged-in user's cart
    @Override
    public BigDecimal getTotal(Principal principal) {
        BigDecimal total = new BigDecimal("0.00");
        total = getTax(principal).add(getSubtotal(principal));
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // Creates and sets attributes for CartTotal object to be displayed to current user
    @Override
    public CartTotal getCartTotal(Principal principal) {
        CartTotal cartTotal = new CartTotal();
        cartTotal.setSubTotal(getSubtotal(principal));
        cartTotal.setTaxAmount(getTax(principal));
        cartTotal.setTotal(getTotal(principal));
        return cartTotal;
    }

    // Helper method that returns a single CartItem
    private CartItem getCartItem(int id) {
        String sql = "SELECT cart_item_id, user_id, c.product_id, quantity, price, name FROM cart_item c " +
                "JOIN product p ON p.product_id = c.product_id " +
                "WHERE cart_item_id = ?";
        SqlRowSet results = jdbc.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToCartItem(results);
        }
        else {
            return null;
        }
    }

    // Helper method that maps CartItem object
    private CartItem mapRowToCartItem(SqlRowSet results) {
        CartItem item = new CartItem();
        item.setCartItemId(results.getInt("cart_item_id"));
        item.setUserId(results.getInt("user_id"));
        item.setProductId(results.getInt("product_id"));
        item.setQuantity(results.getInt("quantity"));
        item.setPrice(results.getBigDecimal("price"));
        item.setProductName(results.getString("name"));
        return item;
    }

    // Helper method that returns current user
    private User getUser(Principal principal) {
        int userId = getUserId(principal);
        return dao.getUserById(userId);
    }

    // Helper method that returns current userId
    private int getUserId(Principal principal) {
        String username = principal.getName();
        int userId = dao.findIdByUsername(username);
        return userId;
    }

}
