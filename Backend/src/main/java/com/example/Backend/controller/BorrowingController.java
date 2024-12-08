package com.example.Backend.controller;

import com.example.Backend.Entity.BorrowedBooks;
import com.example.Backend.Entity.Book;
import com.example.Backend.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BorrowingService borrowingService;

    @Autowired
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/borrowBook")
    public ResponseEntity<String> borrowBook(@RequestParam String userEmail, @RequestParam String bookIsbn) {
        try {
            BorrowedBooks borrowedBook = borrowingService.borrowBook(userEmail, bookIsbn);
            return ResponseEntity.ok("Book borrowed successfully. Due date: " + borrowedBook.getDueDate());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/borrowedBooks")
    public ResponseEntity<List<BorrowedBooks>> getUserBorrowedBooks(@RequestParam String userEmail) {
        List<BorrowedBooks> borrowedBooks = borrowingService.getUserBorrowedBooks(userEmail);
        return ResponseEntity.ok(borrowedBooks);
    }

    @GetMapping("/availableBooks")
    public ResponseEntity<List<Book>> getUserAvailableBooks(@RequestParam String userEmail) {
        List<Book> availableBooks = borrowingService.getUserAvailableBooks(userEmail);
        return ResponseEntity.ok(availableBooks);
    }

    @PostMapping("/sendOverdueNotifications")
    public ResponseEntity<String> sendOverdueNotifications(@RequestParam String userEmail) {
        borrowingService.sendOverdueNotifications(userEmail);
        return ResponseEntity.ok("Overdue notifications sent.");
    }
}
