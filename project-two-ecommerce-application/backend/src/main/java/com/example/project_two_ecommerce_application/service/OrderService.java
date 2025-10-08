package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.Order;

import java.util.List;

public interface OrderService {

     Order placeOrder(int userId, String identifier);

     List<Order> getUserOrders(int userId);
}
