package com.example.project_two_ecommerce_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "CartItem ID")
    private int id;

    // Stores either the User ID (for logged-in) or the Session ID (for guests)
    @Schema(description = "CartItem identifier")
    @Column(nullable = false)
    private String identifier;

    @Schema(description = "CartItem mapping to product table")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Schema(description = "CartItem quantity")
    @Column(nullable = false)
    private int quantity;

}
