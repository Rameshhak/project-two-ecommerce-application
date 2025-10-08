package com.example.project_two_ecommerce_application.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID")
    private int id;

    @Schema(description = "User name")
    @Column(nullable = false)
    private String name;

    @Schema(description = "User email")
    @Column(nullable = false)
    private String email;

    @Schema(description = "User password")
    @Column(nullable = false)
    private String password;

    @Schema(description = "User role")
    @Column(nullable = false)
    private String role;

}
