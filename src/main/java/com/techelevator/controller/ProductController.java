package com.techelevator.controller;

import com.techelevator.dao.JdbcProductDao;
import com.techelevator.dao.ProductDao;
import com.techelevator.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * ProductController class handles all HTTP requests for retrieving products
 */

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/products")
public class ProductController {

    private ProductDao dao;

    public ProductController(ProductDao dao) {
        this.dao = dao;
    }

    // list all products, accepts name and sku as parameters to list specific products
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Product> list(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String sku) {
        if (!name.equals("")) {
            return dao.listProductsByName(name);
        }
        else if (!sku.equals("")) {
            return dao.listProductsBySku(sku);
        }
        else {
            return dao.listAllProducts();
        }
    }

    // Gets a specified product by product id
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Product get(@PathVariable int id) {
        Product product = dao.getProduct(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist");
        }
        else {
            return product;
        }
    }

}
