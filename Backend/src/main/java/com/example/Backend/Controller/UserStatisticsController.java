package com.example.Backend.controller;

import com.example.Backend.service.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/user-statistics")
public class UserStatisticsController {

    @Autowired
    private UserStatisticsService userStatisticsService;

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getUserBorrowingHistory(@RequestParam String userEmail) {
        System.out.println("Received Email: " + userEmail);
        Map<String, Object> history = userStatisticsService.getUserBorrowingHistory(userEmail);
        return ResponseEntity.ok(history);
    }
}
