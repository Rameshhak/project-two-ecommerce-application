package com.example.project_two_ecommerce_application.repository;

import com.example.project_two_ecommerce_application.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByIdentifier(String identifier);

    void deleteByIdentifier(String identifier); // Used after checkout
}
