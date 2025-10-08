package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.CartItem;
import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.repository.CartItemRepository;
import com.example.project_two_ecommerce_application.repository.ProductRepository;
import com.example.project_two_ecommerce_application.service.serviceImpl.CartItemServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceImplTests {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartItemServiceImpl cartService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @DisplayName("getCartItems() → should return cart items for given identifier")
    @Test
    void givenIdentifier_whenGetCartItems_thenReturnCartItems() {
        // Arrange
        String identifier = "user-123";

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("iPhone 15");
        product1.setPrice("99999.0");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Apple Watch");
        product2.setPrice("499.0");

        CartItem item1 = new CartItem();
        item1.setId(1);
        item1.setIdentifier(identifier);
        item1.setProduct(product1);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setId(2);
        item2.setIdentifier(identifier);
        item2.setProduct(product2);
        item2.setQuantity(3);

        List<CartItem> mockCartItems = Arrays.asList(item1, item2);

        when(cartItemRepository.findByIdentifier(identifier)).thenReturn(mockCartItems);

        // Act
        List<CartItem> result = cartService.getCartItems(identifier);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(item1, item2);

        // Verify repository interaction
        verify(cartItemRepository, times(1)).findByIdentifier(identifier);
    }

    @DisplayName("getCartItems() → should return empty list if no cart items found")
    @Test
    void givenIdentifierWithNoItems_whenGetCartItems_thenReturnEmptyList() {
        String identifier = "user-999";

        when(cartItemRepository.findByIdentifier(identifier)).thenReturn(Arrays.asList());

        List<CartItem> result = cartService.getCartItems(identifier);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(cartItemRepository, times(1)).findByIdentifier(identifier);
    }

    @DisplayName("getIdentifier() → should return existing identifier from session")
    @Test
    void givenSessionWithIdentifier_whenGetIdentifier_thenReturnExisting() {
        // Arrange
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("identifier")).thenReturn("EXISTING_ID");

        // Act
        String result = cartService.getIdentifier(request);

        // Assert
        assertThat(result).isEqualTo("EXISTING_ID");

        verify(session, times(1)).getAttribute("identifier");
        verify(session, never()).setAttribute(anyString(), any());
    }

    @DisplayName("getIdentifier() → should generate new identifier if not in session")
    @Test
    void givenSessionWithoutIdentifier_whenGetIdentifier_thenGenerateNew() {
        // Arrange
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("identifier")).thenReturn(null);

        // Act
        String result = cartService.getIdentifier(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(32); // UUID without dashes
        assertThat(result).isUpperCase();

        verify(session, times(1)).getAttribute("identifier");
        verify(session, times(1)).setAttribute(eq("identifier"), eq(result));
    }

    @DisplayName("addItem() → should add a new cart item if not present")
    @Test
    void givenNewProduct_whenAddItem_thenItemIsAdded() {
        String identifier = "user-123";
        int productId = 1;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setName("iPhone 15");
        product.setPrice("99999.0");

        CartItem savedItem = new CartItem();
        savedItem.setIdentifier(identifier);
        savedItem.setProduct(product);
        savedItem.setQuantity(quantity);

        // Mocks
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByIdentifier(identifier))
                .thenReturn(Arrays.asList())              // first call: empty cart
                .thenReturn(Arrays.asList(savedItem));   // second call: cart contains saved item
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedItem);

        // Act
        List<CartItem> result = cartService.addItem(identifier, productId, quantity);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(quantity);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(productId);

        // Verify interactions
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(productRepository, times(1)).findById(productId);
    }

    @DisplayName("addItem() → should update quantity if item already exists")
    @Test
    void givenExistingProduct_whenAddItem_thenQuantityIsUpdated() {
        String identifier = "user-123";
        int productId = 1;
        int existingQuantity = 2;
        int addQuantity = 3;

        Product product = new Product();
        product.setId(productId);
        product.setName("iPhone 15");
        product.setPrice("99999.0");

        CartItem existingItem = new CartItem();
        existingItem.setIdentifier(identifier);
        existingItem.setProduct(product);
        existingItem.setQuantity(existingQuantity);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByIdentifier(identifier)).thenReturn(Arrays.asList(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemRepository.findByIdentifier(identifier)).thenReturn(Arrays.asList(existingItem));

        // Act
        List<CartItem> result = cartService.addItem(identifier, productId, addQuantity);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(existingQuantity + addQuantity);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(productRepository, times(1)).findById(productId);
    }

    @DisplayName("removeItem() → should delete cart item by ID")
    @Test
    void givenCartItemId_whenRemoveItem_thenDeleteIsCalled() {
        int cartItemId = 1;

        // Act
        cartService.removeItem(cartItemId);

        // Assert / Verify
        verify(cartItemRepository, times(1)).deleteById(cartItemId);
    }
}

