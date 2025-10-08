package com.example.project_two_ecommerce_application.repository;

import com.example.project_two_ecommerce_application.entity.Order;
import com.example.project_two_ecommerce_application.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        // Order for userId = 1
        Order order1 = new Order();
        order1.setUserId(1);
        order1.setOrderDate(LocalDateTime.now());
        order1.setTotalAmount(1200.50);
        order1.setStatus(OrderStatus.PENDING);

        // Another order for userId = 1
        Order order2 = new Order();
        order2.setUserId(1);
        order2.setOrderDate(LocalDateTime.now());
        order2.setTotalAmount(850.75);
        order2.setStatus(OrderStatus.SHIPPED);

        // Order for userId = 2
        Order order3 = new Order();
        order3.setUserId(2);
        order3.setOrderDate(LocalDateTime.now());
        order3.setTotalAmount(600.00);
        order3.setStatus(OrderStatus.DELIVERED);

        orderRepository.saveAll(List.of(order1, order2, order3));
        orderRepository.flush();
    }

    @Test
    void testFindByUserId_ReturnsOrdersForSpecificUser() {
        List<Order> orders = orderRepository.findByUserId(1);

        assertThat(orders)
                .isNotEmpty()
                .hasSize(2)
                .allMatch(order -> order.getUserId() == 1);
    }

    @Test
    void testFindByUserId_ReturnsEmptyForNonExistingUser() {
        List<Order> orders = orderRepository.findByUserId(999);

        assertThat(orders).isEmpty();
    }
}
