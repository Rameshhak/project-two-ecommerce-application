package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.CartItem;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(String identifier);

    String getIdentifier(HttpServletRequest request);

    List<CartItem> addItem(String identifier, int productId, int quantity);

    void removeItem(int cartItemId);
}
