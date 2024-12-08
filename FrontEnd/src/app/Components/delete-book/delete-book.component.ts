import { Component, OnInit } from '@angular/core';
import { BookService } from '../../Services/book.service';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from "../navbar/navbar.component";

@Component({
  selector: 'app-delete-book',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './delete-book.component.html',
  styleUrls: ['./delete-book.component.css'],
})
export class DeleteBookComponent implements OnInit {
  books: any[] = [];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.bookService.getBooks(0, 100).subscribe(
      (data) => {
        this.books = data.content;
      },
      (error) => {
        console.error('Error fetching books:', error);
      }
    );
  }

  deleteBook(bookId: number): void {
    this.bookService.deleteBook(bookId).subscribe(
      () => {
        alert('Book deleted successfully!');
        this.books = this.books.filter((book) => book.bookId !== bookId);
      },
      (error) => {
        alert('Failed to delete the book. Please try again.');
      }
    );
  }
}
