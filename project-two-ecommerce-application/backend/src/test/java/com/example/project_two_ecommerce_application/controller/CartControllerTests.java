package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.CartItem;
import com.example.project_two_ecommerce_application.entity.Product;
import com.example.project_two_ecommerce_application.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @MockBean
    private Authentication authentication;

    @Test
    @DisplayName("Add item to cart: should return CartUpdateResponse with total count")
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    void addItemToCart_shouldReturnUpdatedCartCount() throws Exception {
        CartController.AddItemRequest request = new CartController.AddItemRequest();
        request.setProductId(1);
        request.setQuantity(3);

        CartItem item1 = new CartItem();
        item1.setId(1);
        item1.setQuantity(3);

        // Mock service to return a list with one item
        Mockito.when(cartService.addItem(eq("user"), eq(1), eq(3)))
                .thenReturn(List.of(item1));

        mockMvc.perform(post("/api/cart/add")
                        .with(csrf()) // important if CSRF enabled
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Item added successfully."))
                .andExpect(jsonPath("$.newCartCount").value(3));
    }

    @Test
    @DisplayName("Get cart items: should return list of cart items")
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    void getCartItems_shouldReturnList() throws Exception {

        Product product = new Product();
        product.setId(101);
        product.setName("iPhone 15");
        product.setPrice("99999.0");
        product.setDescription("Latest iPhone");

        CartItem item1 = new CartItem();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setId(2);
        item2.setProduct(product);
        item2.setQuantity(1);

        // **Important:** The username must match @WithMockUser
        Mockito.when(cartService.getCartItems("user")).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }



    @Test
    @DisplayName("Delete cart item: should return success message")
    @WithMockUser(username = "testuser", roles = {"CUSTOMER"})
    void deleteCartItem_shouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(cartService).removeItem(1);

        mockMvc.perform(delete("/api/cart/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart item deleted successfully!!!"));
    }
}