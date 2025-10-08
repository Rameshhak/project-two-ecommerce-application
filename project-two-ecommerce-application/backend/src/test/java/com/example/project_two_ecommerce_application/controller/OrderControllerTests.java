package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.Order;
import com.example.project_two_ecommerce_application.entity.User;
import com.example.project_two_ecommerce_application.service.OrderService;
import com.example.project_two_ecommerce_application.service.serviceImpl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Place Order: Given user session, should place order and return 200 OK")
    @WithMockUser(username = "user", roles = {"CUSTOMER"}) //
    void givenUserSession_whenPlaceOrder_thenReturnOrder() throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setTotalAmount(1500.0);

        Mockito.when(orderService.placeOrder(anyInt(), anyString())).thenReturn(order);

        mockMvc.perform(post("/api/orders/place")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Get My Orders: Given user session, should return list of orders with status 200 OK")
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    void givenUserSession_whenGetMyOrders_thenReturnOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1);
        order1.setTotalAmount(1000.0);

        Order order2 = new Order();
        order2.setId(2);
        order2.setTotalAmount(2000.0);

        List<Order> orders = List.of(order1, order2);
        Mockito.when(orderService.getUserOrders(anyInt())).thenReturn(orders);

        mockMvc.perform(get("/api/orders/my")
                        .sessionAttr("JSESSIONID", "mock-session-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(order1.getId()))
                .andExpect(jsonPath("$[1].id").value(order2.getId()));
    }
}
