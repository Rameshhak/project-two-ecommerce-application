package com.example.project_two_ecommerce_application.service;

import com.example.project_two_ecommerce_application.entity.User;

public interface UserService {

     User createUser(User user);

     User getUserById(Integer id);

     User updateUserProfile(User user);
}
