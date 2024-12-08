package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String notificationType;

    @Column(nullable = false)
    private LocalDate timestamp;
}
