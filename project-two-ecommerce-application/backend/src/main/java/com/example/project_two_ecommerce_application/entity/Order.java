package com.example.project_two_ecommerce_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Order ID")
    private int id;

    @Schema(description = "Order Mapping to user table")
    @Column(name = "user_id")
    private int userId; // Foreign key to the User table

    @Schema(description = "Order date")
    @Column(nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Schema(description = "Order total amount")
    @Column(nullable = false)
    private double totalAmount;

    @Schema(description = "Order status")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    // Links to the individual items in this order
    @Schema(description = "Order table mapping to OrderDetail table")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
}
