package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.User;
import com.example.project_two_ecommerce_application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private AuthenticationManager authenticationManager;

        @MockBean
        private com.example.project_two_ecommerce_application.security.JwtService jwtService;

        @MockBean
        private UserRepository userRepository;

        @Test
        @DisplayName("Login: Valid credentials should return token and userId")
        void loginWithValidCredentials_shouldReturnTokenAndUserId() throws Exception {
            // Mock request
            AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
            loginRequest.setName("user");
            loginRequest.setPassword("password");

            // Mock authentication
            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.isAuthenticated()).thenReturn(true);
            Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);

            // Mock user repository
            User user = new User();
            user.setId(1);
            user.setName("user");
            user.setRole("CUSTOMER");

            Mockito.when(userRepository.findByName("user")).thenReturn(Optional.of(user));

            // Mock JWT service
            Mockito.when(jwtService.generateToken(user.getName(), user.getRole(), user.getId()))
                    .thenReturn("mock-jwt-token");

            // Perform POST request
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                    .andExpect(jsonPath("$.userId").value(1));
        }

        @Test
        @DisplayName("Login: Invalid credentials should return 401")
        void loginWithInvalidCredentials_shouldReturn401() throws Exception {
            AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
            loginRequest.setName("wrongUser");
            loginRequest.setPassword("wrongPassword");

            Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new RuntimeException("Bad credentials"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("Invalid username or password"));
        }
}