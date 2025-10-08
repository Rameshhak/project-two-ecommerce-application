package com.example.project_two_ecommerce_application.service.serviceImpl;

import com.example.project_two_ecommerce_application.entity.CartItem;
import com.example.project_two_ecommerce_application.entity.Order;
import com.example.project_two_ecommerce_application.entity.OrderDetail;
import com.example.project_two_ecommerce_application.entity.OrderStatus;
import com.example.project_two_ecommerce_application.repository.CartItemRepository;
import com.example.project_two_ecommerce_application.repository.OrderDetailRepository;
import com.example.project_two_ecommerce_application.repository.OrderRepository;
import com.example.project_two_ecommerce_application.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

     private OrderRepository orderRepository;
     private OrderDetailRepository orderDetailRepository;
     private CartItemRepository cartItemRepository;

    @Override
    public Order placeOrder(int userId, String identifier) {
        // Fetch Cart Items
        List<CartItem> cartItems = cartItemRepository.findByIdentifier(identifier);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        // Calculate Total
        double total = cartItems.stream()
                .mapToDouble(item -> Double.parseDouble(item.getProduct().getPrice()) * item.getQuantity())
                .sum();

        // Create the new Order record
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        // Create Order Details (Line Items)
        List<OrderDetail> details = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            // Important: Use the product's current price as the unit price for the record
            detail.setUnitPrice(Double.parseDouble(item.getProduct().getPrice()));
            return detail;
        }).collect(Collectors.toList());

        orderDetailRepository.saveAll(details);
        savedOrder.setOrderDetails(details);

        // Clear the Cart
        cartItemRepository.deleteByIdentifier(identifier);

        return savedOrder;
    }

    @Override
    public List<Order> getUserOrders(int userId) {
        return orderRepository.findByUserId(userId);
    }
}
