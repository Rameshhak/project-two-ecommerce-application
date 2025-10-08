package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.User;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @DisplayName("Register User: Given valid user, should return status 201 CREATED")
    void givenValidUser_whenRegisterUser_thenReturnCreated() throws Exception {
        // Arrange
        User user = new User();
        user.setName("ramesh");
        user.setEmail("ramesh@example.com");
        user.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setName(user.getName());
        savedUser.setEmail(user.getEmail());
        savedUser.setPassword(user.getPassword());

        Mockito.when(userService.createUser(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)) // send original user
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ramesh"))
                .andExpect(jsonPath("$.email").value("ramesh@example.com"));
    }

    @Test
    @DisplayName("Get User by ID: Given valid ID, should return user and status 200 OK")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenValidId_whenGetUserById_thenReturnUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("ramesh");
        user.setEmail("ramesh@example.com");

        Mockito.when(userService.getUserById(anyInt())).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/user/get-user/{id}", 1) // corrected path
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("Update User: Given valid user and ID, should return updated user and status 200 OK")
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Mock authentication
    void givenValidUserAndId_whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        // Arrange
        User userToUpdate = new User();
        userToUpdate.setName("Ramesh Updated");
        userToUpdate.setEmail("ramesh.updated@example.com");
        userToUpdate.setPassword("newpassword123");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName(userToUpdate.getName());
        updatedUser.setEmail(userToUpdate.getEmail());
        updatedUser.setPassword(userToUpdate.getPassword());

        Mockito.when(userService.updateUserProfile(any(User.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(put("/api/user/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()));
    }

}
