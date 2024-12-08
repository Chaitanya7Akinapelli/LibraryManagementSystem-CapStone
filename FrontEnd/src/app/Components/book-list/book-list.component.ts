import { Component, OnInit } from '@angular/core';
import { BookService } from '../../Services/book.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../Services/user.service';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {
  books: any[] = [];
  searchQuery = { title: '', author: '', genre: ''};
  newBook = { title: '', author: '', genre: '', publicationYear: null, isbn: '', copiesAvailable: null };
  currentPage = 0;
  totalPages = 1;
  resultsCount = 0;
  userEmail: string | null = null;

  constructor(private bookService: BookService, private userService: UserService) {}

  ngOnInit(): void {
    this.userEmail = this.userService.getUserEmail();
    console.log(this.userEmail);
    this.getPaginatedBooks(this.currentPage);
  }

  getBooks(): void {
    this.bookService.getBooks(0, 10).subscribe((data) => {
      this.books = data;
      alert('Books loaded successfully!');
    }, (error) => {
      alert('Failed to load books. Please try again.');
    });
  }

  searchBooks(): void {
    const page = this.currentPage;  // Use the current page for search results
    const size = 10;
    const email = this.userEmail ?? '';
  
    // Make the request with page and size along with the search query
    this.bookService.searchBooks(this.searchQuery, email, page, size).subscribe((data) => {
      this.books = data.content;  // Paginated result content
      this.resultsCount = data.totalElements;  // The total count of results
  
      // Alert on search completion
      if (this.resultsCount > 0) {
        alert(`Found ${this.resultsCount} books matching your search.`);
      } else {
        alert('No books found matching your search.');
      }
  
      if (this.userEmail) {
        console.log(this.userEmail);
        this.bookService.getSearchLogs(this.searchQuery, this.userEmail, this.resultsCount).subscribe(() => {
          console.log('Search logs updated successfully');
        });
      } else {
        console.error('User email is not available');
      }
    }, (error) => {
      alert('Search failed. Please try again.');
    });
  }

  deleteBook(bookId: number): void {
    this.bookService.deleteBook(bookId).subscribe({
      next: () => {
        this.getPaginatedBooks(this.currentPage);  // Refresh after deletion
        alert('Book deleted successfully!');
      },
      error: (error) => {
        alert('Failed to delete the book. Please try again.');
      },
    });
  }

  addBook(): void {
    this.bookService.addBook(this.newBook).subscribe(() => {
      this.getPaginatedBooks(this.currentPage);  // Refresh after adding
      alert('New book added successfully!');
      this.resetNewBookForm();  // Clear form after adding the book
    }, (error) => {
      alert('Failed to add the book. Please try again.');
    });
  }

  getPaginatedBooks(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.bookService.getBooks(page, 10).subscribe((data) => {
        this.books = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
      }, (error) => {
        alert('Failed to load books for the page. Please try again.');
      });
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage > 0) {
      this.getPaginatedBooks(this.currentPage - 1);
    } else {
      alert('You are already on the first page.');
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.getPaginatedBooks(this.currentPage + 1);
    } else {
      alert('You are already on the last page.');
    }
  }

  // Helper function to reset the new book form
  resetNewBookForm(): void {
    this.newBook = { title: '', author: '', genre: '', publicationYear: null, isbn: '', copiesAvailable: null };
  }
}
