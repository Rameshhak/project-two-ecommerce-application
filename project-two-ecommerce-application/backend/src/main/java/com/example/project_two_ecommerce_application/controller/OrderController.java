package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.Order;
import com.example.project_two_ecommerce_application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@Tag(name = "OrderController",
       description = "Manage the order placement process and retrieve the orders from the database.")
public class OrderController {

    private OrderService orderService;

    // NOTE: This should get the actual logged-in user ID from authentication context
    private int getUserId(HttpServletRequest request) {
        // Placeholder: Assuming a dummy user ID for now
        // In a real app, this would be from JWT/SecurityContext
        return 1;
    }

    // Assuming the checkout process is simply placing the order from the cart
    @Operation(summary = "Place the order REST API",
                  description = "Order placing and stored in the database.")
    @ApiResponse(responseCode = "200",
                  description = "HTTP Status 200 OK")
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(HttpServletRequest request) {
        int userId = getUserId(request);
        // Reuse the cart identifier to fetch the items for the order
        String identifier = request.getSession().getId();

        Order newOrder = orderService.placeOrder(userId, identifier);
        return ResponseEntity.ok(newOrder);
    }

    @Operation(summary = "Display the order REST API",
            description = "Retrieve the all orders from the database.")
    @ApiResponse(responseCode = "200",
            description = "HTTP Status 200 OK")
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(HttpServletRequest request) {
        int userId = getUserId(request);
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
}
