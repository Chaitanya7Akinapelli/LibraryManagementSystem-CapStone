package com.example.Backend.service;

import java.util.Map;

public interface UserStatisticsService {
    Map<String, Object> getUserBorrowingHistory(String userEmail);
}
