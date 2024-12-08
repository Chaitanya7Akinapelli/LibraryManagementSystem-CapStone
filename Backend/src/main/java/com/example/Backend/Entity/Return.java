package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "returns")
public class Return{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;

    private String userEmail;

    private String bookIsbn;

    private LocalDate returnDate;

    private double penaltyAmount;

    private String processedBy;

}
