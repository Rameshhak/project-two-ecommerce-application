package com.example.project_two_ecommerce_application.repository;

import com.example.project_two_ecommerce_application.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp(){
        // given
        productRepository.deleteAll();
        Product product1 = new Product();
        product1.setName("Apple iPhone 15");
        product1.setPrice("99999.0");
        product1.setDescription("Latest iPhone with A17 chip mobile");

        Product product2 = new Product();
        product2.setName("Samsung Galaxy S24");
        product2.setPrice("69999.0");
        product2.setDescription("Flagship Android phone mobile");

        Product product3 = new Product();
        product3.setName("Apple Watch");
        product3.setPrice("499.0");
        product3.setDescription("Smartwatch with health features");

        productRepository.saveAll(List.of(product1,product2,product3));

        productRepository.flush();
    }

    @Test
    @DisplayName("Should return products matching name or description (case-insensitive)")
    void testSearchByNameOrDescription() {
        productRepository.flush();

        // when
        List<Product> results = productRepository.searchByNameOrDescription("apple");

        // then
        assertThat(results)
                .isNotEmpty()
                .hasSize(2); // iPhone + Watch

        assertThat(results)
                .extracting(Product::getName)
                .contains("Apple iPhone 15", "Apple Watch");
    }

    @Test
    @DisplayName("Should return empty list when no match found")
    void testSearchByNameOrDescription_NoMatch() {
        // when
        List<Product> results = productRepository.searchByNameOrDescription("nokia");

        // then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should match by description as well")
    void testSearchByDescription() {
        // when
        List<Product> results = productRepository.searchByNameOrDescription("android");

        // then
        assertThat(results)
                .isNotEmpty()
                .hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Samsung Galaxy S24");
    }
}
