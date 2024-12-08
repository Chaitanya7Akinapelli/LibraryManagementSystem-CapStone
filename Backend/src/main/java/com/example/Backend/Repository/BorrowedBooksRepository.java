package com.example.Backend.Repository;

import com.example.Backend.Entity.BorrowedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowedBooksRepository extends JpaRepository<BorrowedBooks, Long> {
    List<BorrowedBooks> findByUserEmail(String userEmail);

    List<BorrowedBooks> findByUserEmailAndBookIsbn(String userEmail, String bookIsbn);

    @Query("SELECT b FROM BorrowedBooks b WHERE b.dueDate < :currentDate AND b.status = 'BORROWED'")
    List<BorrowedBooks> findOverdueBooks(LocalDate currentDate);

    List<BorrowedBooks> findBooksByUserEmail(String userEmail);
    Optional<BorrowedBooks>findByUserEmailAndBookIsbnAndStatus(String userEmail, String bookIsbn, String status);

    @Query("SELECT b.bookIsbn FROM BorrowedBooks b GROUP BY b.bookIsbn ORDER BY COUNT(b.bookIsbn) DESC")
    List<String> findPopularBooks();

    @Query("SELECT b FROM BorrowedBooks b WHERE b.dueDate < :currentDate AND b.returnDate IS NULL")
    List<BorrowedBooks> findOverdueRecords(LocalDate currentDate);

    @Query("SELECT COUNT(b) FROM BorrowedBooks b WHERE b.status = 'BORROWED'")
    long countNotReturnedBooks();

    @Query("SELECT COUNT(b) FROM BorrowedBooks b WHERE b.dueDate < :currentDate AND b.status = 'BORROWED'")
    long countOverdueBooks(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT b.bookIsbn, COUNT(b) FROM BorrowedBooks b WHERE b.userEmail = :userEmail GROUP BY b.bookIsbn ORDER BY COUNT(b) DESC")
    List<Object[]> findMostBorrowedBookByUser(String userEmail);

}
