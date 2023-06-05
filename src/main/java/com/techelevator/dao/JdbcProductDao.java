package com.techelevator.dao;

import com.techelevator.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProductDao implements ProductDao{

    private final JdbcTemplate jdbc;

    // Returns list of all products
    public JdbcProductDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    @Override
    public List<Product> listAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        SqlRowSet results = jdbc.queryForRowSet(sql);
        while (results.next()) {
            Product p = mapRowToProduct(results);
            products.add(p);
        }
        return products;
    }

    // Returns list of products with specified string anywhere in their name
    @Override
    public List<Product> listProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE name LIKE CONCAT( '%',?,'%')";
        SqlRowSet results = jdbc.queryForRowSet(sql, name);
        while (results.next()) {
            Product p = mapRowToProduct(results);
            products.add(p);
        }
        return products;
    }

    // Returns list of products with specified string anywhere in their sku
    @Override
    public List<Product> listProductsBySku(String sku) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE product_sku LIKE CONCAT( '%',?,'%')";
        SqlRowSet results = jdbc.queryForRowSet(sql, sku);
        while (results.next()) {
            Product p = mapRowToProduct(results);
            products.add(p);
        }
        return products;
    }

    // Returns a single product with specified id
    @Override
    public Product getProduct(int id) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        SqlRowSet results = jdbc.queryForRowSet(sql, id);
        if (results.next()) {
            Product p = mapRowToProduct(results);
            return p;
        }
        return null;
    }

    // Helper method that maps Product object
    private Product mapRowToProduct(SqlRowSet results) {
        Product product = new Product();
        product.setProductId(results.getInt("product_id"));
        product.setSku(results.getString("product_sku"));
        product.setName(results.getString("name"));
        if (results.getString("description") != null) {
            product.setDescription(results.getString("description"));
        }
        product.setPrice(results.getBigDecimal("price"));
        if (results.getString("image_name") != null) {
            product.setImageName(results.getString("image_name"));
        }
        return product;
    }

}
