package com.example.project_two_ecommerce_application.controller;

import com.example.project_two_ecommerce_application.entity.User;
import com.example.project_two_ecommerce_application.service.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag( name = "CRUD APIs for user resource",
        description = "Manages user related actions like create, read, update APIs.")
public class UserController {

      private UserServiceImpl userService;

      @Operation(
              summary = "Create user REST API",
              description = "The Create User REST API is used to save a user in the database."
      )
      @ApiResponse(
              responseCode = "201",
              description = "HTTP Status 201 CREATED"
      )
      @PostMapping("register")
      public ResponseEntity<User> registerUser(@RequestBody User user){
          User savedUser = userService.createUser(user);
          return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
      }

      @Operation(
            summary = "Display user REST API",
            description = "Display user info by their ID get from database."
      )
      @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
      )
      @GetMapping("/get-user/{id}")
      public ResponseEntity<User> displayProductById(@PathVariable("id") Integer id){
          User user = userService.getUserById(id);
          return new ResponseEntity<>(user, HttpStatus.OK);
      }

      @Operation(
            summary = "Update user REST API",
            description = "Update user info by their ID and get and modify it in the database."
      )
      @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
      )
      @PutMapping("/update/{id}")
      public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user){
          user.setId(id);

          User updatedUser = userService.updateUserProfile(user);
          return new ResponseEntity<>(updatedUser, HttpStatus.OK);
      }

}
