import { Component } from '@angular/core';
import { BookService } from '../../Services/book.service';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-book',
  standalone: true,
  imports: [FormsModule, NavbarComponent , CommonModule],
  templateUrl: './add-book.component.html',
  styleUrls: ['./add-book.component.css']
})
export class AddBookComponent {
  newBook = { title: '', author: '', genre: '', publicationYear: null, isbn: '', copiesAvailable: null };
  isbnError: string | null = null;

  constructor(private bookService: BookService) {}

  validateISBN(isbn: string): boolean {
    if (!/^(978|979)\d{10}$/.test(isbn)) {
      this.isbnError = 'ISBN must be 13 digits and start with 978 or 979.';
      return false;
    }

    const digits = isbn.split('').map(Number);
    const sum = digits
      .slice(0, 12)
      .reduce((acc, digit, index) => acc + digit * (index % 2 === 0 ? 1 : 3), 0);
    const checkDigit = (10 - (sum % 10)) % 10;
    if (checkDigit !== digits[12]) {
      this.isbnError = 'Invalid ISBN check digit.';
      return false;
    }

    this.isbnError = null;
    return true;
  }

  addBook(): void {
    if (this.validateISBN(this.newBook.isbn)) {
      this.bookService.addBook(this.newBook).subscribe(() => {
        alert('New book added successfully!');
        this.resetNewBookForm();
      }, (error) => {
        alert('Failed to add the book. Please try again.');
      });
    } else {
      alert('Invalid ISBN');
    }
  }

  resetNewBookForm(): void {
    this.newBook = { title: '', author: '', genre: '', publicationYear: null, isbn: '', copiesAvailable: null };
    this.isbnError = null;
  }
}
