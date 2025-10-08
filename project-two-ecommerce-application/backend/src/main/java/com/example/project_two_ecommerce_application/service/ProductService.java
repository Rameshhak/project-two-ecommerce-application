package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(Integer id);

    List<Product> displayAllProducts();

    List<Product> searchProducts(String query);

    Product updateProductDetails(Product product);

    void deleteProductDetails(Integer id);
}
