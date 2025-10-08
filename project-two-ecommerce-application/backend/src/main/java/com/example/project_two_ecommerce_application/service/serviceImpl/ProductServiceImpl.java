package com.example.project_two_ecommerce_application.service.serviceImpl;

import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.repository.ProductRepository;
import com.example.project_two_ecommerce_application.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id " + id));
    }

    @Override
    public List<Product> displayAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String query) {
        // Delegate the complex query logic to the JpaRepository
        return productRepository.searchByNameOrDescription(query);
    }

    @Override
    public Product updateProductDetails(Product product) {
       Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new RuntimeException("Invalid id"));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProductDetails(Integer id) {
        productRepository.deleteById(id);
    }
}
