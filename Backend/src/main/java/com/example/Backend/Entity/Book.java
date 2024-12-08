package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;
    private String author;
    private String genre;
    private Integer publicationYear;
    @Column(unique = true)
    private String isbn;
    private Integer copiesAvailable;

    public boolean isAvailable() {
        return copiesAvailable != null && copiesAvailable > 0;
    }

    public void setAvailable(boolean available) {
        if (available)
        {
            this.copiesAvailable = (this.copiesAvailable == null) ? 1 : this.copiesAvailable + 1;
        }
        else{
            if (this.copiesAvailable == null || this.copiesAvailable <= 0) {
                throw new IllegalStateException("No copies available to mark as unavailable.");
            }
            this.copiesAvailable--;
        }
    }
}
