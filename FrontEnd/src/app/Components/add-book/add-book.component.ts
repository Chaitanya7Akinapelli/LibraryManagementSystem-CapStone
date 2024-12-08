import { Component } from '@angular/core';
import { BookService } from '../../Services/book.service';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";
@Component({
  selector: 'app-add-book',
  standalone: true,
  imports: [FormsModule, NavbarComponent],
  templateUrl: './add-book.component.html',
  styleUrl: './add-book.component.css'
})
export class AddBookComponent {
  newBook = { title: '', author: '', genre: '', publicationYear: null, isbn: '', copiesAvailable: null };

  constructor(private bookService: BookService) {}

  addBook(): void {
    this.bookService.addBook(this.newBook).subscribe(() => {
      alert('New book added successfully!');
      this.resetNewBookForm();
    }, (error) => {
      alert('Failed to add the book. Please try again.');
    });
  }

  resetNewBookForm(): void {
    this.newBook = { title: '', author: '', genre: '', publicationYear: null, isbn: '', copiesAvailable: null };
  }
}
