import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { title } from 'process';
import { NavbarComponent } from "../navbar/navbar.component";
@Component({
  selector: 'app-reports-chart',
  standalone : true,
  imports: [FormsModule, CommonModule, NavbarComponent],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {
  reports: any[] = [];
  kpiArray: any[] = [];
  additionalMetricsArray: any[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadReports();
    this.loadKpi();
    this.loadAdditionalMetrics();
  }

  loadReports(): void {
    this.userService.getAllReports().subscribe((reports) => {
      this.reports = reports;
    });
  }

  loadKpi(): void {
    this.userService.getKpi().subscribe((kpi) => {
      const mostSearchedBookString = kpi.mostSearchedBook;
      const bookDetails = this.parseBookDetails(mostSearchedBookString);
      
      this.kpiArray = [
        { value: kpi.totalActiveUsers, label: 'Total Active Users', color: '#4CAF50' },
        { value: kpi.totalAvailableBooks, label: 'Available Books', color: '#2196F3' },
        { value: bookDetails.title, label: 'Most Searched Book Title', color: '#FFC107' },
        { value: bookDetails.author, label: 'Most Searched Book Author', color: '#FF5722' },
        { value: bookDetails.genre, label: 'Most Searched Book Genre', color: '#9C27B0' }
      ];
  
      console.log(bookDetails);
    });
  }
  
  parseBookDetails(bookString: string): { title: string, author: string, genre: string } {
    const bookDetails = { title: '', author: '', genre: '' };
  
    const parts = bookString.split(',');

    parts.forEach(part => {
      const [key, value] = part.split('=');
      if (key && value !== undefined) {
        if (key.trim() === 'title') {
          bookDetails.title = value.trim();
        } else if (key.trim() === 'author') {
          bookDetails.author = value.trim();
        } else if (key.trim() === 'genre') {
          bookDetails.genre = value.trim();
        }
      }
    });
  
    return bookDetails;
  }
  

  loadAdditionalMetrics(): void {
    this.userService.getAdditionalMetrics().subscribe((metrics) => {
      this.additionalMetricsArray = [
        { value: metrics.totalBorrowings, label: 'Total Borrowings', color: '#FF5722' },
        { value: metrics.totalOverdueBooks, label: 'Overdue Books', color: '#E91E63' },
        { value: metrics.totalNotReturnedBooks, label: 'Not Returned Books', color: '#9C27B0' }
      ];
    });
  }
}
