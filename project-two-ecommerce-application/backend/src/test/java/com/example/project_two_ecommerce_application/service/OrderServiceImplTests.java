package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.*;
import com.example.project_two_ecommerce_application.repository.CartItemRepository;
import com.example.project_two_ecommerce_application.repository.OrderDetailRepository;
import com.example.project_two_ecommerce_application.repository.OrderRepository;
import com.example.project_two_ecommerce_application.service.serviceImpl.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @DisplayName("placeOrder() → should place order successfully")
    @Test
    void givenCartItems_whenPlaceOrder_thenOrderIsPlaced() {
        // Arrange
        String identifier = "user-123";
        int userId = 1;

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
        item1.setProduct(product1);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setId(2);
        item2.setProduct(product2);
        item2.setQuantity(3);

        List<CartItem> cartItems = Arrays.asList(item1, item2);

        when(cartItemRepository.findByIdentifier(identifier)).thenReturn(cartItems);

        Order savedOrder = new Order();
        savedOrder.setId(100);
        savedOrder.setUserId(userId);
        savedOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(100);
                    return order;
                });
        when(orderDetailRepository.saveAll(anyList())).thenReturn(null); // saveAll returns void

        // Act
        Order result = orderService.placeOrder(userId, identifier);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100);
        assertThat(result.getOrderDetails()).hasSize(2);

        // Verify total calculation
        double expectedTotal = 2 * 99999.0 + 3 * 499.0;
        assertThat(result.getTotalAmount()).isEqualTo(expectedTotal);

        // Verify repository interactions
        verify(cartItemRepository, times(1)).findByIdentifier(identifier);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderDetailRepository, times(1)).saveAll(anyList());
        verify(cartItemRepository, times(1)).deleteByIdentifier(identifier);
    }

    @DisplayName("placeOrder() → should throw exception when cart is empty")
    @Test
    void givenEmptyCart_whenPlaceOrder_thenThrowException() {
        String identifier = "user-123";
        int userId = 1;

        when(cartItemRepository.findByIdentifier(identifier)).thenReturn(Arrays.asList());

        assertThatThrownBy(() -> orderService.placeOrder(userId, identifier))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cart is empty!");

        verify(orderRepository, never()).save(any(Order.class));
        verify(orderDetailRepository, never()).saveAll(anyList());
        verify(cartItemRepository, times(1)).findByIdentifier(identifier);
        verify(cartItemRepository, never()).deleteByIdentifier(identifier);
    }

    @DisplayName("getUserOrders() → should return list of orders for given userId")
    @Test
    void givenUserId_whenGetUserOrders_thenReturnOrders() {
        // Arrange
        int userId = 1;

        Order order1 = new Order();
        order1.setId(101);
        order1.setUserId(userId);
        order1.setStatus(OrderStatus.PENDING);

        Order order2 = new Order();
        order2.setId(102);
        order2.setUserId(userId);
        order2.setStatus(OrderStatus.DELIVERED);

        List<Order> mockOrders = Arrays.asList(order1, order2);

        when(orderRepository.findByUserId(userId)).thenReturn(mockOrders);

        // Act
        List<Order> result = orderService.getUserOrders(userId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(order1, order2);

        // Verify repository interaction
        verify(orderRepository, times(1)).findByUserId(userId);
    }

    @DisplayName("getUserOrders() → should return empty list if user has no orders")
    @Test
    void givenUserIdWithNoOrders_whenGetUserOrders_thenReturnEmptyList() {
        int userId = 99;

        when(orderRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        List<Order> result = orderService.getUserOrders(userId);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(orderRepository, times(1)).findByUserId(userId);
    }
}

