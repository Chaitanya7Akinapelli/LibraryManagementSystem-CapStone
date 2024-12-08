package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "search_logs")
public class SearchLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchId;

    private String userEmail;
    private String searchQuery;
    private LocalDateTime date;
    private Integer resultsCount;
}
