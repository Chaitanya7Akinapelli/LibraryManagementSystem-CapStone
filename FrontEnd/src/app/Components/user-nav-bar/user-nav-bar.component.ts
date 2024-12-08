import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-user-nav-bar',
  standalone: true,
  imports: [],
  templateUrl: './user-nav-bar.component.html',
  styleUrl: './user-nav-bar.component.css'
})
export class UserNavBarComponent {
  constructor(private router: Router){}

  goToBorrowBooks(){
    this.router.navigate(['/borrowbooks']);
  }
  goToBorrowingHistory(){
    this.router.navigate(['/borrowinghistory']);
  }
}
