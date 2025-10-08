package com.example.project_two_ecommerce_application.repository;


import com.example.project_two_ecommerce_application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Define the custom search query using HQL (Hibernate Query Language)
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchByNameOrDescription(@Param("query") String query);
}
