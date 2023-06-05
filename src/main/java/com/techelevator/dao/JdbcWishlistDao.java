package com.techelevator.dao;

import com.techelevator.model.Product;
import com.techelevator.model.WishList;
import com.techelevator.model.WishlistItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcWishlistDao implements WishListDao{

    private final JdbcTemplate jdbc;
    private final UserDao dao;

    public JdbcWishlistDao (JdbcTemplate jdbc, UserDao dao) {
        this.jdbc = jdbc;
        this.dao = dao;
    }

    // Returns list of all user wishlists and sets wishlist items for each list
    @Override
    public List<WishList> getAllWishlists(Principal principal) {
        List<WishList> wishlists = new ArrayList<>();
        String sql = "SELECT * FROM wishlist " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbc.queryForRowSet(sql, getUserId(principal));
        while (results.next()) {
            WishList wishList = mapRowToWishlist(results);
            List<WishlistItem> items = getWishlistItems(principal, wishList.getWishListId());
            wishList.setItems(items);
            wishlists.add(wishList);
        }
        return wishlists;
    }

    // Returns a single wishlist for user and sets items for list
    @Override
    public WishList getWishList(int wishlistId, Principal principal) {
        WishList wishList = new WishList();
        String sql = "SELECT * FROM wishlist " +
                "WHERE user_id = ? " +
                "AND wishlist_id = ?";
        SqlRowSet results = jdbc.queryForRowSet(sql, getUserId(principal), wishlistId);
        if (results.next()) {
            wishList = mapRowToWishlist(results);
            List<WishlistItem> items = getWishlistItems(principal, wishlistId);
            wishList.setItems(items);
        }
        return wishList;
    }

    // Helper method that returns a list of items for given wishlist
    public List<WishlistItem> getWishlistItems(Principal principal, int wishlistId) {
        List<WishlistItem> items = new ArrayList<>();
        String sql = "SELECT w.wishlist_id, wi.wishlist_item_id, wi.product_id, p.name, p.price" +
                " FROM wishlist w " +
                " JOIN wishlist_item wi ON wi.wishlist_id = w.wishlist_id " +
                "JOIN product p ON p.product_id = wi.product_id " +
                "WHERE w.wishlist_id = ? " +
                "AND w.user_id = ?";
        SqlRowSet results = jdbc.queryForRowSet(sql, wishlistId, getUserId(principal));
        while (results.next()) {
            WishlistItem item = mapRowToWishListItem(results);
            items.add(item);
        }
        return items;
    }

    // creates new user wishlist and populates list with items
    @Override
    public WishList createWishlist(WishList wishList, Principal principal) {
        String wishlistSql = "INSERT INTO wishlist (name, user_id) " +
                "VALUES (?, ?) " +
                "RETURNING wishlist_id";
        int newListId = jdbc.queryForObject(wishlistSql, Integer.class, wishList.getName(), getUserId(principal));
        List<WishlistItem> items = wishList.getItems();
        for (WishlistItem item : items) {
            addItem(newListId, item.getProductId(), principal);
        }
        return getWishList(newListId, principal);
    }

    // adds an item to a wishlist, if item is already inside the specified list, the item is deleted and re-added
    // so no errors are thrown
    public WishlistItem addItem(int wishlistId, int productId, Principal principal) {
        String sql = "INSERT INTO wishlist_item (wishlist_id, product_id) " +
                "VALUES (?, ?) " +
                "RETURNING wishlist_item_id";
        List<WishlistItem> items = getWishlistItems(principal, wishlistId);
        for (WishlistItem item : items) {
            if (item.getProductId() == productId) {
                String deleteSql = "DELETE FROM wishlist_item " +
                        "WHERE product_id = ?";
                jdbc.update(deleteSql, productId);
            }
        }
        int newItemId = jdbc.queryForObject(sql, Integer.class, wishlistId, productId);
        return getItem(productId);
    }

    // Deletes a specified wishlist belonging to the user
    @Override
    public void deleteWishlist(int wishlistId, Principal principal) {
        String itemDeleteSql = "DELETE FROM wishlist_item " +
                "WHERE wishlist_id = ?";
        jdbc.update(itemDeleteSql, wishlistId);
        String sql = "DELETE FROM wishlist " +
                "WHERE wishlist_id = ? " +
                "AND user_id = ?";
        jdbc.update(sql, wishlistId, getUserId(principal));
    }

    // Deletes an item from a specified wishlist
    @Override
    public void removeItem(int wishlistId, int productId) {
        String sql = "DELETE FROM wishlist_item " +
                "WHERE product_id = ? " +
                "AND wishlist_id = ?";
        jdbc.update(sql, productId, wishlistId);
    }

    // Helper method that creates Wishlist object
    private WishList mapRowToWishlist(SqlRowSet results) {
        WishList wishList = new WishList();
        wishList.setWishListId(results.getInt("wishlist_id"));
        wishList.setUserId(results.getInt("user_id"));
        wishList.setName(results.getString("name"));
        return wishList;
    }

    // Helper method that creates WishlistItem object
    public WishlistItem mapRowToWishListItem(SqlRowSet results) {
        WishlistItem item = new WishlistItem();
        item.setWishlistItemId(results.getInt("wishlist_item_id"));
        item.setWishlistId(results.getInt("wishlist_id"));
        item.setProductId(results.getInt("product_id"));
        item.setName(results.getString("name"));
        item.setPrice(results.getBigDecimal("price"));
        return item;
    }

    // Helper method that returns the current user id
    private int getUserId(Principal principal) {
        String username = principal.getName();
        int userId = dao.findIdByUsername(username);
        return userId;
    }

    // Helper method that returns a WishlistItem from productId
    private WishlistItem getItem(int productId) {
        WishlistItem item = new WishlistItem();
        String sql = "SELECT wishlist_item_id, wishlist_id, w.product_id, p.name, p.price FROM wishlist_item w " +
                "JOIN product p ON p.product_id = w.product_id " +
                "WHERE p.product_id = ?";
        SqlRowSet results = jdbc.queryForRowSet(sql, productId);
        if (results.next()) {
            item = mapRowToWishListItem(results);
        }
        return item;
    }
}
