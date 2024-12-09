package com.example.Backend.service.IMPL;

import com.example.Backend.Entity.Users;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.dto.UserDTO;
import com.example.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        // Convert UserDTO to User Entity
        Users user = new Users();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        Users savedUser = userRepository.save(user);
        System.out.println(savedUser);
        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setName(savedUser.getName());
        savedUserDTO.setEmail(savedUser.getEmail());
        savedUserDTO.setPhoneNumber(savedUser.getPhoneNumber());
        savedUserDTO.setRole(savedUser.getRole());

        return savedUserDTO;
    }

    @Override
    public boolean loginUser(String email, String password) {
        Users user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAdmin(String email) {
        Users user = userRepository.findByEmail(email);
        if (user != null && "admin".equals(user.getRole())) {
            return true;
        }
        return false;
    }

}
