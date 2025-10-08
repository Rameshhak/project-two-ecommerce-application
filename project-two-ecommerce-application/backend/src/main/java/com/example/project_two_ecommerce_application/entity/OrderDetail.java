package com.example.project_two_ecommerce_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "OrderDetail ID")
    private int id;

    @Schema(description = "OrderDetail mapping to order table")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Schema(description = "OrderDetail mapping to product table")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Schema(description = "OrderDetail quantity")
    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double unitPrice; // Price at the time of sale

}
