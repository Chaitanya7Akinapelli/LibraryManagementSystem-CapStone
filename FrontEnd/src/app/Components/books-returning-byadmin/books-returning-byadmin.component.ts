import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from "../navbar/navbar.component";

@Component({
  selector: 'app-books-returning-byadmin',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './books-returning-byadmin.component.html',
  styleUrls: ['./books-returning-byadmin.component.css']
})
export class BooksReturningByadminComponent implements OnInit {
  userEmail: string | null = null;
  adminEmail: string | null = null;
  borrowedBooks: any[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.adminEmail = this.userService.getUserEmail();
    if (this.adminEmail) {
      console.log(this.adminEmail);
    }
  }

  fetchBorrowedBooks() {
    if (this.userEmail) {
      this.userService.getBorrowedBooks(this.userEmail).subscribe(
        (books) => {
          console.log(books);
          this.borrowedBooks = books;
        },
        (error) => {
          console.error('Error fetching borrowed books:', error);
        }
      );
    }
  }

  returnBook(bookIsbn: string) {
    if (this.adminEmail && this.userEmail) {
      this.userService.returnBookByAdmin(this.userEmail, bookIsbn, this.adminEmail).subscribe(
        (response) => {
          alert('Book returned successfully');
          this.fetchBorrowedBooks();
        },
        (error) => {
          console.error('Error returning book:', error);
        }
      );
    }
  }
  
}
