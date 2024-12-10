package com.example.Backend.service;

import com.example.Backend.Entity.BorrowedBooks;
import com.example.Backend.Entity.Book;

import java.util.List;

public interface BorrowingService {
    BorrowedBooks borrowBook(String userEmail, String bookIsbn);
    List<BorrowedBooks> getUserBorrowedBooks(String userEmail);
    List<Book> getUserAvailableBooks(String userEmail);
    void sendOverdueNotifications(String userEmail);
    void returnBookByAdmin(String userEmail , String bookIsbn , String adminEmail);
}
