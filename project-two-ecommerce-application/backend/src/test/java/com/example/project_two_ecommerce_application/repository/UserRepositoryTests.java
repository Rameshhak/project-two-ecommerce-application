package com.example.project_two_ecommerce_application.repository;

import com.example.project_two_ecommerce_application.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Test for get user by name operation")
    @Test
     void givenUsername_whenGetUserByName_thenReturnUser(){
        // given
        User user = new User();
        user.setName("Rahul");
        user.setEmail("rahul@gmail.com");
        user.setPassword("rahul");
        user.setRole("ROLE_CUSTOMER");
        userRepository.save(user);

        //when
        Optional<User> getUser = userRepository.findByName(user.getName());

        //then
        assertThat(getUser).isNotNull();
        assertThat(user.getId()).isGreaterThan(0);

    }
}
