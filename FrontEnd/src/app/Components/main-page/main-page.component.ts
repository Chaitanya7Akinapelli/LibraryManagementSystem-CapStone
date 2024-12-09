import { Component } from '@angular/core';
import { NavbarComponent } from "../navbar/navbar.component";
import { BookService } from '../../Services/book.service';
import { UserService } from '../../Services/user.service';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent {
  books: any[] = [];
  currentPage = 0;
  totalPages = 1;
  resultsCount = 0;

  constructor(private bookService: BookService, private userService: UserService) {}

  ngOnInit(): void {
    this.getPaginatedBooks(this.currentPage);
  }

  getPaginatedBooks(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.bookService.getBooks(page, 10).subscribe((data) => {
        this.books = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
      });
    }
  }
}
