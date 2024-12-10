package com.example.Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    // Getters and Setters
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
}
