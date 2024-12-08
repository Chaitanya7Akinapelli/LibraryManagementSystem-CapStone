package com.example.Backend.Repository;

import com.example.Backend.Entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    Page<Book> findByAuthorContaining(String author, Pageable pageable);
    Page<Book> findByGenreContaining(String genre, Pageable pageable);
    Page<Book> findByTitleContainingAndAuthorContainingAndGenreContaining(String title, String author, String genre, Pageable pageable);
    Page<Book> findByTitleContainingAndAuthorContaining(String title, String author, Pageable pageable);
    Page<Book> findByTitleContainingAndGenreContaining(String title, String genre, Pageable pageable);
    Page<Book> findByAuthorContainingAndGenreContaining(String author, String genre, Pageable pageable);
    Book findByIsbn(String isbn);

    // Correctly place this query here
    @Query("SELECT b FROM Book b WHERE b.isbn NOT IN :isbns AND b.copiesAvailable > 0")
    List<Book> findAvailableBooksNotBorrowed(@Param("isbns") List<String> isbns);

    @Query("SELECT SUM(b.copiesAvailable) FROM Book b")
    Integer sumCopiesAvailable();
}

