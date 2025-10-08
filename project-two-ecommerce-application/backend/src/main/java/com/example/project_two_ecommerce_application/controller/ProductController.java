package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.service.serviceImpl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@Tag( name = "CRUD REST APIs for products",
        description = "Manage the product info actions of CRUD REST APIs.")
public class ProductController {

    private ProductServiceImpl productService;

    @Operation( summary = "Create product REST API",
                description = "The Create User REST API is used to save a user in the database.")
    @ApiResponse(responseCode = "201",
                 description = "HTTP Status 201 CREATED")
    @PostMapping("/create")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Display product REST API",
            description = "Display product info by their ID get from database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @GetMapping("/get-product/{id}")
    public ResponseEntity<Product> displayProductById(@PathVariable("id") Integer id){
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Operation(
            summary = "Display all products REST API",
            description = "Display every products info from database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @GetMapping("/display-all-products")
    public ResponseEntity<List<Product>> displayAllProducts(){
        List<Product> listedProducts = productService.displayAllProducts();
        return new ResponseEntity<>(listedProducts, HttpStatus.OK);
    }

    @Operation(
            summary = "Display, Search product REST API",
            description = "Display product by their ID and Search product info by keyword get from database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @GetMapping // Maps to the base path: /api/products
    public ResponseEntity<List<Product>> getHomePageProductsAndSearch(
            // CRITICAL FIX: required = false allows the API call to work without 'q'
            @RequestParam(value = "q", required = false) String searchQuery) {

        List<Product> resultProducts;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // If query is present, perform search
            resultProducts = productService.searchProducts(searchQuery.trim());
        } else {
            // If no query (homepage load), return all products
            // NOTE: We can use the service method for the existing 'displayAllProducts' API here
            resultProducts = productService.displayAllProducts();
        }

        return new ResponseEntity<>(resultProducts, HttpStatus.OK);
    }

    @Operation(
            summary = "Update product REST API",
            description = "Update product info by ID and get and modify it in the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @PostMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product){
        product.setId(id);

        Product updatedProduct = productService.updateProductDetails(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product REST API",
            description = "Delete product info by ID and remove it from the database."
    )
    @ApiResponse(
            responseCode = "204",
            description = "HTTP Status 204 NO_CONTENT"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer id){
        productService.deleteProductDetails(id);

        return new ResponseEntity<>("product deleted successfully!!!", HttpStatus.NO_CONTENT);
    }
}
