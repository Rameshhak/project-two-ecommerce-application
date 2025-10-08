package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.User;
import com.example.project_two_ecommerce_application.repository.UserRepository;
import com.example.project_two_ecommerce_application.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "AuthenticationController",
        description = "Manage the authentication process to log in the user.")
@CrossOrigin(origins="https://frabjous-belekoy-4bc68d.netlify.app/", allowCredentials="true")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private final JwtService jwtService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getName(),
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                // ✅ Get the authenticated user
                User user = userRepository.findByName(request.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                // ✅ Generate JWT using JwtService
                String token = jwtService.generateToken(user.getName(), user.getRole(), user.getId());

                // ✅ Return token and userId
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "userId", user.getId()
                ));
            } else {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid credentials"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }


    @Data
    public static class LoginRequest{

        private String name;
        private String password;
    }

}

