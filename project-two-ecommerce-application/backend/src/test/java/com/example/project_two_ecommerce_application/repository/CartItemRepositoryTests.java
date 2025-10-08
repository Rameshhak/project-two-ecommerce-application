package com.example.project_two_ecommerce_application.repository;

import com.example.project_two_ecommerce_application.entity.CartItem;
import com.example.project_two_ecommerce_application.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class CartItemRepositoryTests {

        @Autowired
        private CartItemRepository cartItemRepository;

        @Autowired
        private ProductRepository productRepository;

        private Product product1;
        private Product product2;

        @BeforeEach
        void setUp() {
            cartItemRepository.deleteAll();
            productRepository.deleteAll();

            // Create and save some products
            product1 = new Product();
            product1.setName("Apple iPhone 15");
            product1.setPrice("99999.0");
            product1.setDescription("Latest iPhone with A17 chip");
            productRepository.save(product1);

            product2 = new Product();
            product2.setName("Samsung Galaxy S24");
            product2.setPrice("69999.0");
            product2.setDescription("Flagship Android phone");
            productRepository.save(product2);

            // Create and save cart items with identifiers
            CartItem item1 = new CartItem();
            item1.setIdentifier("user123");
            item1.setProduct(product1);
            item1.setQuantity(2);

            CartItem item2 = new CartItem();
            item2.setIdentifier("user123");
            item2.setProduct(product2);
            item2.setQuantity(1);

            CartItem item3 = new CartItem();
            item3.setIdentifier("guest456");
            item3.setProduct(product2);
            item3.setQuantity(3);

            cartItemRepository.saveAll(List.of(item1, item2, item3));
            cartItemRepository.flush();
        }

        @DisplayName("Test that we should give cart items if we have an existing identifier.")
        @Test
        void testFindByIdentifier_ShouldReturnCorrectCartItems() {
            // when
            List<CartItem> items = cartItemRepository.findByIdentifier("user123");

            // then
            assertThat(items).isNotEmpty().hasSize(2)
                    .allMatch(item -> item.getIdentifier().equals("user123"));
        }

        @DisplayName("Test that should give 0 cart items if the identifier didn't have those products.")
        @Test
        void testFindByIdentifier_ShouldReturnEmptyForUnknownIdentifier() {
            // when
            List<CartItem> items = cartItemRepository.findByIdentifier("unknownUser");

            // then
            assertThat(items).isEmpty();
        }

        @DisplayName("Test for delete cart items: the user wants to delete products in the cart")
        @Test
        void testDeleteByIdentifier_ShouldRemoveItemsForGivenIdentifier() {
            // when
            cartItemRepository.deleteByIdentifier("user123");

            List<CartItem> remaining = cartItemRepository.findByIdentifier("user123");
            List<CartItem> guestItems = cartItemRepository.findByIdentifier("guest456");

            // then
            assertThat(remaining).isEmpty(); // user123 items deleted
            assertThat(guestItems).isNotEmpty(); // guest456 items remain
        }
}
