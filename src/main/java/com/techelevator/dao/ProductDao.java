package com.techelevator.dao;

import com.techelevator.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> listAllProducts();
    List<Product> listProductsByName(String name);
    List<Product> listProductsBySku(String sku);
    Product getProduct(int productId);
}
