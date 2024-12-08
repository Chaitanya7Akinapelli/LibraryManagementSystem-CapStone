import { Component } from '@angular/core';
import { BookService } from '../../Services/book.service';
import { UserService } from '../../Services/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from "../navbar/navbar.component";
@Component({
  selector: 'app-search-book',
  standalone: true,
  imports: [FormsModule, CommonModule, NavbarComponent],
  templateUrl: './search-book.component.html',
  styleUrl: './search-book.component.css'
})
export class SearchBookComponent {
  searchQuery = { title: '', author: '', genre: '' };
  books: any[] = [];
  resultsCount = 0;
  userEmail: string | null = null;
  currentPage = 0;

  constructor(private bookService: BookService, private userService: UserService) {}

  ngOnInit(): void {
    this.userEmail = this.userService.getUserEmail();
  }

  searchBooks(): void {
    const page = this.currentPage;
    const size = 10;
    const email = this.userEmail ?? '';
  
    this.bookService.searchBooks(this.searchQuery, email, page, size).subscribe((data) => {
      this.books = data.content;
      this.resultsCount = data.totalElements;

      if (this.resultsCount > 0) {
        alert(`Found ${this.resultsCount} books matching your search.`);
        this.resetNewBookForm();
      } else {
        alert('No books found matching your search.');
      }

      if (this.userEmail) {
        this.bookService.getSearchLogs(this.searchQuery, this.userEmail, this.resultsCount).subscribe(() => {
          console.log('Search logs updated successfully');
        });
      }
    }, (error) => {
      alert('Search failed. Please try again.');
    });
  }
  resetNewBookForm(): void {
    this.searchQuery = {title  :'' , author: '', genre: ''};
  }
}
