package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.service.serviceImpl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductServiceImpl productService;

    @DisplayName("POST /api/products/create → should create a product successfully")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenProduct_whenCreateProduct_thenReturnCreatedProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("iPhone 15");
        product.setPrice("99999.0");
        product.setDescription("Latest iPhone");

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.price").value("99999.0"));
    }

    @DisplayName("GET /api/products/get-product/{id} → should get a product by id successfully from db")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenProductId_whenGetProduct_thenReturnProduct() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("iPhone 15");
        product.setPrice("99999.0");
        product.setDescription("Latest iPhone");

        Mockito.when(productService.getProductById(anyInt())).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/products/get-product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.price").value("99999.0"))
                .andExpect(jsonPath("$.description").value("Latest iPhone"));
    }

    @DisplayName("GET /api/products/get-product/{id} → should get message if product not found by id")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidProductId_whenGetProduct_thenReturnNotFound() throws Exception {
        // Arrange: mock product not found
        Mockito.when(productService.getProductById(anyInt()))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));

        // Act & Assert
        mockMvc.perform(get("/api/products/get-product/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

        @DisplayName("GET /api/products/display-all-products → should get all products successfully")
        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void givenProducts_whenDisplayAllProducts_thenReturnProductList() throws Exception {
            // Arrange
            Product product1 = new Product();
            product1.setId(1);
            product1.setName("iPhone 15");
            product1.setPrice("99999.0");
            product1.setDescription("Latest iPhone");

            Product product2 = new Product();
            product2.setId(2);
            product2.setName("Samsung Galaxy S24");
            product2.setPrice("89999.0");
            product2.setDescription("Latest Samsung Phone");

            List<Product> productList = Arrays.asList(product1, product2);

            when(productService.displayAllProducts()).thenReturn(productList);

            // Act & Assert
            mockMvc.perform(get("/api/products/display-all-products")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("iPhone 15"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].name").value("Samsung Galaxy S24"));
        }

        @DisplayName("GET /api/products/display-all-products → should get message if products are empty")
        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void givenNoProducts_whenDisplayAllProducts_thenReturnEmptyList() throws Exception {
            // Arrange: return empty list
            when(productService.displayAllProducts()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/products/display-all-products")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));
        }

        @DisplayName("GET /api/products → should show all products if no query entered")
        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void givenNoQuery_whenGetProducts_thenReturnAllProducts() throws Exception {
            // Arrange
            Product product1 = new Product();
            product1.setId(1);
            product1.setName("iPhone 15");
            product1.setPrice("99999.0");
            product1.setDescription("Latest iPhone");

            Product product2 = new Product();
            product2.setId(2);
            product2.setName("Samsung Galaxy S24");
            product2.setPrice("89999.0");
            product2.setDescription("Latest Samsung Phone");

            List<Product> allProducts = Arrays.asList(product1, product2);
            when(productService.displayAllProducts()).thenReturn(allProducts);

            // Act & Assert
            mockMvc.perform(get("/api/products")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].name").value("iPhone 15"))
                    .andExpect(jsonPath("$[1].name").value("Samsung Galaxy S24"));
        }

        @DisplayName("GET /api/products → should show filtered products to home when search specific keyword is entered")
        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void givenSearchQuery_whenGetProducts_thenReturnFilteredProducts() throws Exception {
            // Arrange
            Product product1 = new Product();
            product1.setId(1);
            product1.setName("iPhone 15");
            product1.setPrice("99999.0");
            product1.setDescription("Latest iPhone");

            List<Product> searchResults = Arrays.asList(product1);
            when(productService.searchProducts("iPhone")).thenReturn(searchResults);

            // Act & Assert
            mockMvc.perform(get("/api/products")
                            .param("q", "iPhone")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].name").value("iPhone 15"));
        }

        @DisplayName("GET /api/products → should show all products to home when no search keyword is entered")
        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void givenEmptySearchQuery_whenGetProducts_thenReturnAllProducts() throws Exception {
            // Arrange
            Product product1 = new Product();
            product1.setId(1);
            product1.setName("iPhone 15");

            List<Product> allProducts = Arrays.asList(product1);
            when(productService.displayAllProducts()).thenReturn(allProducts);

            // Act & Assert
            mockMvc.perform(get("/api/products")
                            .param("q", "   ") // blank query
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].name").value("iPhone 15"));
        }

    @Test
    @DisplayName("Update Product: Given valid product ID and details, should return updated product with status 200")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidProductIdAndDetails_whenUpdateProduct_thenReturnUpdatedProduct() throws Exception {
        // Arrange
        Integer productId = 1;
        Product productToUpdate = new Product();
        productToUpdate.setName("iPhone 15 Pro");
        productToUpdate.setPrice("109999.0");
        productToUpdate.setDescription("Updated iPhone");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("iPhone 15 Pro");
        updatedProduct.setPrice("109999.0");
        updatedProduct.setDescription("Updated iPhone");

        when(productService.updateProductDetails(Mockito.any(Product.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products/update/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"))
                .andExpect(jsonPath("$.price").value("109999.0"))
                .andExpect(jsonPath("$.description").value("Updated iPhone"));
    }

    @Test
    @DisplayName("Delete Product: Given valid product ID, should return status 204 NO_CONTENT")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidProductId_whenDeleteProduct_thenReturnNoContent() throws Exception {
        // Arrange
        Integer productId = 1;
        doNothing().when(productService).deleteProductDetails(productId);

        // Act & Assert
        mockMvc.perform(delete("/api/products/delete/{id}", productId))
                .andExpect(status().isNoContent());
    }
}
