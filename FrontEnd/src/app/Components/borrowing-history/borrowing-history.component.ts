import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { UserStatisticsServiceService } from '../../Services/user-statistics-service.service';
import { CommonModule } from '@angular/common';
import { UserNavBarComponent } from "../user-nav-bar/user-nav-bar.component";
@Component({
  selector: 'app-borrowing-history',
  standalone: true,
  imports: [CommonModule, UserNavBarComponent],
  templateUrl: './borrowing-history.component.html',
  styleUrl: './borrowing-history.component.css'
})
export class BorrowingHistoryComponent implements OnInit{
  borrowingData: any = null;

  constructor(
    private userStatisticsService: UserStatisticsServiceService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const userEmail = this.userService.getUserEmail();
    console.log("User Email:", userEmail);
    if (userEmail) {
      this.userStatisticsService.getUserBorrowingHistory(userEmail)
      .subscribe(data => {
        this.borrowingData = data;
      }, error => {
        console.error('Error fetching borrowing history', error);
      });
    }
  }  
}
