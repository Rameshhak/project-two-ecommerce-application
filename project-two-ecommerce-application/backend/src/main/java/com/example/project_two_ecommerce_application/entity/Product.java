package com.example.project_two_ecommerce_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Product ID")
    private int id;

    @Schema(description = "Product name")
    @Column(nullable = false)
    private String name;

    @Schema(description = "Product price")
    @Column(nullable = false)
    private String price;

    @Schema(description = "Product description")
    @Column(nullable = false)
    private String description;

}
