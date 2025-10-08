package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.repository.ProductRepository;
import com.example.project_two_ecommerce_application.service.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryImplTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @DisplayName("createProduct() -> save and return product the successfully")
    @Test
    void givenProduct_whenCreateProduct_thenReturnSavedProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("iPhone 15");
        product.setPrice("99999.0");
        product.setDescription("Latest iPhone with A17 chip");
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product savedProduct = productService.createProduct(product);

        // Assert
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(1);
        assertThat(savedProduct.getName()).isEqualTo("iPhone 15");
        assertThat(savedProduct.getPrice()).isEqualTo("99999.0");
        assertThat(savedProduct.getDescription()).isEqualTo("Latest iPhone with A17 chip");

        // verify
        verify(productRepository, times(1)).save(product);
    }

    @DisplayName("getProductById() → should return product when found")
    @Test
    void givenValidId_whenGetProductById_thenReturnProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("iPhone 15");
        product.setPrice("99999.0");
        product.setDescription("Latest iPhone with A17 chip");

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // Act
        Product foundProduct = productService.getProductById(1);

        // Assert
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo("iPhone 15");
        verify(productRepository, times(1)).findById(1);
    }

    @DisplayName("getProductById() → should throw exception when product not found")
    @Test
    void givenInvalidId_whenGetProductById_thenThrowException() {
        // Arrange
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.getProductById(99))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Product not found with id 99");

        verify(productRepository, times(1)).findById(99);
    }

    @DisplayName("displayAllProducts() → should return list of all products")
    @Test
    void givenProducts_whenDisplayAllProducts_thenReturnProductList() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("iPhone 15");
        product1.setPrice("99999.0");
        product1.setDescription("Latest iPhone with A17 chip");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Samsung Galaxy S24");
        product2.setPrice("69999.0");
        product2.setDescription("Flagship Android phone");

        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> result = productService.displayAllProducts();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(product1, product2);

        // Verify repository interaction
        verify(productRepository, times(1)).findAll();
    }

    @DisplayName("searchProducts() → should return list of products matching query")
    @Test
    void givenQuery_whenSearchProducts_thenReturnMatchingProducts() {
        // Arrange
        String query = "Apple";

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Apple iPhone 15");
        product1.setPrice("99999.0");
        product1.setDescription("Latest iPhone with A17 chip");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Apple Watch");
        product2.setPrice("499.0");
        product2.setDescription("Smartwatch with health features");

        List<Product> mockResult = Arrays.asList(product1, product2);

        // Mock repository behavior
        when(productRepository.searchByNameOrDescription(query)).thenReturn(mockResult);

        // Act
        List<Product> result = productService.searchProducts(query);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(product1, product2);

        // Verify repository interaction
        verify(productRepository, times(1)).searchByNameOrDescription(query);
    }


    @DisplayName("searchProducts() → should return empty list when no products match")
    @Test
    void givenQueryWithNoMatch_whenSearchProducts_thenReturnEmptyList() {
        // Arrange
        String query = "NonExistingProduct";
        when(productRepository.searchByNameOrDescription(query)).thenReturn(Arrays.asList());

        // Act
        List<Product> result = productService.searchProducts(query);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(productRepository, times(1)).searchByNameOrDescription(query);
    }

    @DisplayName("updateProductDetails() → should update product successfully")
    @Test
    void givenExistingProduct_whenUpdateProductDetails_thenReturnUpdatedProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1);
        existingProduct.setName("Old iPhone");
        existingProduct.setPrice("89999.0");
        existingProduct.setDescription("Old description");

        Product updatedProduct = new Product();
        updatedProduct.setId(1);
        updatedProduct.setName("iPhone 15");
        updatedProduct.setPrice("99999.0");
        updatedProduct.setDescription("Latest iPhone with A17 chip");

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productService.updateProductDetails(updatedProduct);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("iPhone 15");
        assertThat(result.getPrice()).isEqualTo("99999.0");
        assertThat(result.getDescription()).isEqualTo("Latest iPhone with A17 chip");

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @DisplayName("updateProductDetails() → should throw exception when product not found")
    @Test
    void givenInvalidProductId_whenUpdateProductDetails_thenThrowException() {
        // Arrange
        Product product = new Product();
        product.setId(99);
        product.setName("Non-existing Product");

        when(productRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.updateProductDetails(product))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid id");

        verify(productRepository, times(1)).findById(99);
        verify(productRepository, never()).save(any(Product.class));
    }

    @DisplayName("deleteProductDetails() → should call deleteById() once")
    @Test
    void givenProductId_whenDeleteProductDetails_thenRepositoryDeleteCalled() {
        // Arrange
        Integer productId = 1;

        // Act
        productService.deleteProductDetails(productId);

        // Assert / Verify
        verify(productRepository, times(1)).deleteById(productId);
    }

}
