package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.User;
import com.example.project_two_ecommerce_application.repository.UserRepository;
import com.example.project_two_ecommerce_application.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @DisplayName("Test createUser should encode password and save user")
    @Test
    void givenUser_whenCreateUser_thenUserIsSavedWithEncodedPassword() {
        // arrange
        User user = new User();
        user.setName("Jack");
        user.setEmail("jack@gmail.com");
        user.setPassword("plainPassword");
        user.setRole("ROLE_CUSTOMER");

        String encodedPassword = "encodedPassword123";

        // mock dependencies
        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        User savedUser = userService.createUser(user);

        // assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getName()).isEqualTo("Jack");
        assertThat(savedUser.getRole()).isEqualTo("ROLE_CUSTOMER");

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("getUserById() → should return user when found")
    @Test
    void givenValidId_whenGetUserById_thenReturnUser() {
        // arrange
        User user = new User();
        user.setId(1);
        user.setName("Alice");
        user.setEmail("alice@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole("ROLE_CUSTOMER");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.getUserById(1);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("Alice");
        assertThat(foundUser.getEmail()).isEqualTo("alice@gmail.com");
        verify(userRepository, times(1)).findById(1);
    }

    @DisplayName("getUserById() → should throw exception when user not found")
    @Test
    void givenInvalidId_whenGetUserById_thenThrowException() {
        // Arrange
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(99))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not found with id 99");

        verify(userRepository, times(1)).findById(99);
    }


    @DisplayName("updateUserProfile() → should update existing user details successfully")
    @Test
    void givenExistingUser_whenUpdateUserProfile_thenReturnUpdatedUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("john");
        existingUser.setEmail("john@gmail.com");
        existingUser.setPassword("oldPassword");
        existingUser.setRole("ROLE_ADMIN");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("mark");
        updatedUser.setEmail("mark@gmail.com");
        updatedUser.setPassword("newPassword");
        updatedUser.setRole("ROLE_CUSTOMER");

        String encodedPassword = "encodedPassword123";

        // Mock behavior
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUserProfile(updatedUser);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("mark");
        assertThat(result.getEmail()).isEqualTo("mark@gmail.com");
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        assertThat(result.getRole()).isEqualTo("ROLE_CUSTOMER");

        // Verify interactions
        verify(userRepository, times(1)).findById(1);
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(existingUser);

    }

    @DisplayName("updateUserProfile() → should throw exception when user not found")
    @Test
    void givenInvalidUserId_whenUpdateUserProfile_thenThrowException() {
        // Arrange
        User user = new User();
        user.setId(999);
        user.setName("Unknown");
        user.setEmail("unknown@example.com");
        user.setPassword("password");

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUserProfile(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Id is not found");

        verify(userRepository, times(1)).findById(999);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}

