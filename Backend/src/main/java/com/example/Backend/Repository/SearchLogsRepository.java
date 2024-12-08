package com.example.Backend.Repository;

import com.example.Backend.Entity.SearchLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchLogsRepository extends JpaRepository<SearchLogs, Long> {
    @Query("SELECT s.searchQuery FROM SearchLogs s GROUP BY s.searchQuery ORDER BY COUNT(s.searchQuery) DESC")
    List<String> findMostSearchedBooks();
}
