package com.techelevator.model;

import java.math.BigDecimal;

/**
 * Model class that represents the product database
 */

public class Product {

    private int productId;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageName;

    public Product() {}

    public Product (int productId, String sku, String name, String description, BigDecimal price, String imageName) {
        this.productId = productId;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.price.setScale(2);
        this.imageName = imageName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String  getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        this.price.setScale(2);
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    @Override
    public String toString() {
        return "Product Id: " + productId + "\n" +
                "Sku: " + sku + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Price: " + price + "\n" +
                "Image Name: " + imageName;
    }
}
