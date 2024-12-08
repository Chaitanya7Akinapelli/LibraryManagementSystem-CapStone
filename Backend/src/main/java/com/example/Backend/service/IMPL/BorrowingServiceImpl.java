package com.example.Backend.service.IMPL;

import com.example.Backend.Entity.BorrowedBooks;
import com.example.Backend.Entity.Book;
import com.example.Backend.Entity.Notifications;
import com.example.Backend.Entity.Return;
import com.example.Backend.Repository.BorrowedBooksRepository;
import com.example.Backend.Repository.BooksRepository;
import com.example.Backend.Repository.NotificationsRepository;
import com.example.Backend.Repository.ReturnRepository;
import com.example.Backend.service.BorrowingService;
import com.example.Backend.Util.EmailSenderService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    private final BooksRepository bookRepository;
    private final BorrowedBooksRepository borrowedBooksRepository;
    private final EmailSenderService emailSenderService;
    private final NotificationsRepository notificationsRepository;
    private final ReturnRepository returnRepository;

    public BorrowingServiceImpl(BooksRepository bookRepository, BorrowedBooksRepository borrowedBooksRepository, EmailSenderService emailSenderService, NotificationsRepository notificationsRepository, ReturnRepository returnRepository) {
        this.bookRepository = bookRepository;
        this.borrowedBooksRepository = borrowedBooksRepository;
        this.emailSenderService = emailSenderService;
        this.notificationsRepository = notificationsRepository;
        this.returnRepository = returnRepository;
    }

    @Override
    public BorrowedBooks borrowBook(String userEmail, String bookIsbn) throws IllegalStateException {
        // Check if the book exists and is available
        Book book = bookRepository.findByIsbn(bookIsbn);
        if (book == null) {
            throw new IllegalStateException("Book with ISBN " + bookIsbn + " does not exist.");
        }
        if (!book.isAvailable()) {
            throw new IllegalStateException("This book is currently unavailable.");
        }

        // Check if the user has already borrowed this book and not returned it
        List<BorrowedBooks> borrowedBooks = borrowedBooksRepository.findByUserEmailAndBookIsbn(userEmail, bookIsbn);
        boolean isCurrentlyBorrowed = borrowedBooks.stream()
                .anyMatch(borrowedBook -> "BORROWED".equalsIgnoreCase(borrowedBook.getStatus()));

        if (isCurrentlyBorrowed) {
            throw new IllegalStateException("You have already borrowed this book and haven't returned it yet.");
        }

        // Create a new BorrowedBooks record and save it
        BorrowedBooks newBorrowedBook = new BorrowedBooks();
        newBorrowedBook.setUserEmail(userEmail);
        newBorrowedBook.setBookIsbn(bookIsbn);
        newBorrowedBook.setStatus("BORROWED");
        newBorrowedBook.setBorrowDate(LocalDate.now());
        newBorrowedBook.setDueDate(LocalDate.now().plusDays(1));

        return borrowedBooksRepository.save(newBorrowedBook);
    }

    @Override
    public void returnBookByAdmin(String userEmail, String bookIsbn, String adminEmail) {
        // Find the borrowed book record
        Optional<BorrowedBooks> borrowedBookOptional = borrowedBooksRepository.findByUserEmailAndBookIsbnAndStatus(userEmail, bookIsbn, "BORROWED");

        if (borrowedBookOptional.isEmpty()) {
            throw new IllegalStateException("This book is not borrowed by the user or already returned.");
        }

        BorrowedBooks borrowedBook = borrowedBookOptional.get();

        // Update the borrowed book status to "RETURNED"
        borrowedBook.setReturnDate(LocalDate.now());
        borrowedBook.setStatus("RETURNED");
        borrowedBooksRepository.save(borrowedBook);

        // Update the availability of the book by marking it as available
        Book book = bookRepository.findByIsbn(bookIsbn); // No need for Optional here
        if (book != null) {
            book.setAvailable(true);  // Mark the book as available
            bookRepository.save(book);
        } else {
            throw new IllegalStateException("Book with ISBN " + bookIsbn + " does not exist.");
        }

        // Calculate penalty if the book is overdue
        LocalDate currentDate = LocalDate.now();
        long overdueDays = currentDate.toEpochDay() - borrowedBook.getDueDate().toEpochDay();
        double penaltyAmount = overdueDays > 0 ? overdueDays * 5 : 0; // Example penalty of 5 units per day

        // Create a return entry in the Returns table
        Return returnRecord = new Return();
        returnRecord.setUserEmail(userEmail);
        returnRecord.setBookIsbn(bookIsbn);
        returnRecord.setReturnDate(currentDate);
        returnRecord.setPenaltyAmount(penaltyAmount);
        returnRecord.setProcessedBy(adminEmail);

        returnRepository.save(returnRecord);
    }
    private LocalDate calculateDueDate(LocalDate borrowDate) {
        LocalDate dueDate = borrowDate.plusDays(28); // Set a 28-day due date
        if (dueDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dueDate = dueDate.plusDays(1); // Skip Sunday
        }
        return dueDate;
    }

    @Override
    public List<BorrowedBooks> getUserBorrowedBooks(String userEmail) {
        return borrowedBooksRepository.findByUserEmail(userEmail).stream()
                .filter(borrowedBook -> "BORROWED".equalsIgnoreCase(borrowedBook.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getUserAvailableBooks(String userEmail) {
        // Fetch the list of ISBNs of books already borrowed by the user
        List<String> borrowedIsbns = borrowedBooksRepository.findByUserEmail(userEmail).stream()
                .filter(borrowedBook -> "BORROWED".equalsIgnoreCase(borrowedBook.getStatus()))
                .map(BorrowedBooks::getBookIsbn)
                .collect(Collectors.toList());

        // Fetch books that are available and not already borrowed by the user
        // Correctly query BooksRepository now
        return bookRepository.findAvailableBooksNotBorrowed(borrowedIsbns);
    }

    @Override
    public void sendOverdueNotifications(String userEmail) {
        System.out.println(userEmail);

        // Fetch overdue books
        List<BorrowedBooks> overdueBooks = borrowedBooksRepository.findOverdueBooks(LocalDate.now());

        // Get the current date to compare for overdue books
        LocalDate currentDate = LocalDate.now();

        // Loop through each overdue book and send notification
        overdueBooks.forEach(borrowedBook -> {
            String bookIsbn = borrowedBook.getBookIsbn();
            Book book = bookRepository.findByIsbn(bookIsbn);

            long daysOverdue = currentDate.toEpochDay() - borrowedBook.getDueDate().toEpochDay();
            long penalty = daysOverdue > 0 ? daysOverdue : 0;

            // Create overdue message
            String message = String.format("Dear %s,\n\n"
                            + "Your borrowed book \"%s\" (ISBN: %s) is overdue.\n"
                            + "Due Date: %s\n"
                            + "Overdue by %d day(s). Penalty: ₹%d.\n\n"
                            + "Please return it immediately.",
                    userEmail, book.getTitle(), bookIsbn, borrowedBook.getDueDate(), daysOverdue, penalty);

            // Check if the book is overdue and send the overdue notification
            if (daysOverdue > 0) {
                emailSenderService.sendEmail(userEmail, "Overdue Reminder", message);
                saveNotification(userEmail, message);
            }
        });

        // Fetch all borrowed books and send an immediate email upon borrowing
        List<BorrowedBooks> borrowedBooks = borrowedBooksRepository.findBooksByUserEmail(userEmail);
        borrowedBooks.forEach(borrowedBook -> {
            String bookIsbn = borrowedBook.getBookIsbn();
            Book book = bookRepository.findByIsbn(bookIsbn);

            // Send email immediately after borrowing
            String borrowConfirmationMessage = String.format("Dear %s,\n\n"
                            + "You have successfully borrowed the book \"%s\" (ISBN: %s).\n"
                            + "Due Date: %s\n\n"
                            + "Please ensure to return the book before the due date to avoid penalties.",
                    userEmail, book.getTitle(), bookIsbn, borrowedBook.getDueDate());

            emailSenderService.sendEmail(userEmail, "Borrow Confirmation", borrowConfirmationMessage);

            // Save the notification immediately after borrowing
            String borrowNotificationMessage = String.format("You borrowed \"%s\" (ISBN: %s). Due Date: %s",
                    book.getTitle(), bookIsbn, borrowedBook.getDueDate());
            saveNotification(userEmail, borrowNotificationMessage);
        });
    }

    // Helper method to save notifications to the database
    private void saveNotification(String userEmail, String message) {
        Notifications notification = new Notifications();
        notification.setUserEmail(userEmail);
        notification.setMessage(message);
        notification.setNotificationType("Reminder");
        notification.setTimestamp(LocalDate.now());
        notificationsRepository.save(notification);
    }

}
