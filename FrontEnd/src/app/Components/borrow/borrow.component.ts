import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { BorrowingService } from '../../Services/borrowing.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserNavBarComponent } from "../user-nav-bar/user-nav-bar.component";

@Component({
  selector: 'app-borrow',
  imports: [CommonModule, FormsModule, UserNavBarComponent],
  standalone: true,
  templateUrl: './borrow.component.html',
  styleUrls: ['./borrow.component.css'],
})
export class BorrowComponent implements OnInit {
  availableBooks: any[] = [];
  borrowedBooks: any[] = [];
  userEmail: string | null = null;
  title : string | null = null;
  author : string | null = null;

  constructor(
    private borrowingService: BorrowingService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userEmail = this.userService.getUserEmail();
    if (this.userEmail) {
      console.log(this.userEmail);
      this.loadBooks();
    }
  }

  loadBooks(): void {
    this.borrowingService.getAvailableBooks(this.userEmail!).subscribe({
      next: (books) => {
        console.log('Available books:', books);
        this.availableBooks = books;
      },
      error: (error) => {
        console.error('Error fetching available books:', error); 
        alert('Failed to fetch available books.');
      },
    });

    this.borrowingService.getBorrowedBooks(this.userEmail!).subscribe({
      next: (books) => {
        console.log('Borrowed books:', books);
        this.borrowedBooks = books;
      },
      error: (error) => {
        console.error('Error fetching borrowed books:', error); 
        alert('Failed to fetch borrowed books.');
      },
    });
  }

  borrowBook(isbn: string): void {
    if (!this.userEmail) {
      alert('User email is missing');
      return;
    }
  
    const borrowedBook = this.availableBooks.find(book => book.isbn === isbn);
    if (borrowedBook) {
      this.title = borrowedBook.title;
      this.author = borrowedBook.author;
    }
  
    this.borrowingService.borrowBook(this.userEmail, isbn).subscribe({
      next: (response) => {
        alert(`${response}\nYou have borrowed:\nTitle: ${this.title}\nAuthor: ${this.author}`);
        this.loadBooks();
        this.sendOverdueNotifications();
      },
      error: (error) => {
        console.error('Failed to borrow book:', error);
        alert(error.error || 'Failed to borrow book.');
      },
    });
  }
  

  sendOverdueNotifications(): void {
    if (this.userEmail) {
      this.borrowingService.sendOverdueNotifications(this.userEmail).subscribe({
        next: (response) => {
          console.log('Notification sent:', response);
          alert('Notification sent successfully');
        },
        error: (error) => {
          console.error('Error sending overdue notifications:', error);
          alert('Failed to send overdue notifications.');
        },
      });
    }
  }
}
