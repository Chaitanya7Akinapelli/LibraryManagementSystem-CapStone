package com.example.Backend.controller;

import com.example.Backend.Entity.Book;
import com.example.Backend.Entity.SearchLogs;
import com.example.Backend.Entity.Users;
import com.example.Backend.Repository.BooksRepository;
import com.example.Backend.Repository.SearchLogsRepository;
import com.example.Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BooksRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SearchLogsRepository searchLogsRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/search")
    public Page<Book> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) String genre,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam String userEmail) {

        Page<Book> resultPage;

        // If both title and author or genre are provided, combine them
        if (title != null && author != null && genre != null) {
            resultPage = bookRepository.findByTitleContainingAndAuthorContainingAndGenreContaining(title, author, genre, PageRequest.of(page, size));
        } else if (title != null && author != null) {
            resultPage = bookRepository.findByTitleContainingAndAuthorContaining(title, author, PageRequest.of(page, size));
        } else if (title != null && genre != null) {
            resultPage = bookRepository.findByTitleContainingAndGenreContaining(title, genre, PageRequest.of(page, size));
        } else if (author != null && genre != null) {
            resultPage = bookRepository.findByAuthorContainingAndGenreContaining(author, genre, PageRequest.of(page, size));
        } else if (title != null) {
            resultPage = bookRepository.findByTitleContaining(title, PageRequest.of(page, size));
        } else if (author != null) {
            resultPage = bookRepository.findByAuthorContaining(author, PageRequest.of(page, size));
        } else if (genre != null) {
            resultPage = bookRepository.findByGenreContaining(genre, PageRequest.of(page, size));
        } else {
            resultPage = bookRepository.findAll(PageRequest.of(page, size));
        }

        // Log the search query
        SearchLogs searchLog = new SearchLogs();
        searchLog.setUserEmail(userEmail);
        searchLog.setSearchQuery("title=" + title + ", author=" + author + ", genre=" + genre);
        searchLog.setDate(LocalDateTime.now());
        searchLog.setResultsCount(resultPage.getContent().size());
        searchLogsRepository.save(searchLog);

        return resultPage;
    }


    @GetMapping("/isAdmin")
    public ResponseEntity<Boolean> checkAdminRole(@RequestParam String email) {
        Users user = userRepository.findByEmail(email);
        if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookRepository.save(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public Page<Book> getBooks(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return bookRepository.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/searchLogs")
    public List<SearchLogs> getSearchLogs() {
        return searchLogsRepository.findAll();
    }

}
