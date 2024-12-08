package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "borrowed_books")
public class BorrowedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowId;

    @Column(nullable = false , name = "user_email")
    private String userEmail;

    @Column(nullable = false, name = "book_isbn")
    private String bookIsbn;

    @Column(nullable = false, name = "borrow_date")
    private LocalDate borrowDate;

    @Column(nullable = false, name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = false , name = "status")
    private String status;
}
