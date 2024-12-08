package com.example.Backend.service;

import com.example.Backend.Entity.Users;
import com.example.Backend.dto.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    boolean loginUser(String email, String password);
    boolean isAdmin(String email);
}
