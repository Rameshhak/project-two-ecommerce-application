package com.example.project_two_ecommerce_application.entity;

// Simple Enum for the required statuses
public enum OrderStatus {
    PENDING,        // Waiting for payment/confirmation
    PROCESSING,     // Payment confirmed, preparing shipment
    SHIPPED, DELIVERED       // Order completed
}
