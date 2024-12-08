package com.example.Backend.controller;

import com.example.Backend.Entity.BorrowedBooks;
import com.example.Backend.dto.UserDTO;
import com.example.Backend.service.BorrowingService;
import com.example.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final BorrowingService borrowingService;

    @Autowired
    public UserController(UserService userService, BorrowingService borrowingService) {
        this.userService = userService;
        this.borrowingService = borrowingService;
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        System.out.println("Email: " + email + ", Password: " + password);
        return userService.loginUser(email, password);
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin(@RequestParam String email) {
        return userService.isAdmin(email);
    }

    @PostMapping("/admin/returnBook")
    public void returnBookByAdmin(@RequestParam String userEmail, @RequestParam String bookIsbn, @RequestParam String adminEmail) {
        System.out.println(userEmail + " " + bookIsbn + adminEmail);
        borrowingService.returnBookByAdmin(userEmail, bookIsbn, adminEmail);
    }

    // New API endpoint to fetch all borrowed books by user
    @GetMapping("/admin/borrowedBooks")
    public List<BorrowedBooks> getBorrowedBooksByUser(@RequestParam String userEmail) {
        return borrowingService.getUserBorrowedBooks(userEmail);
    }
}
