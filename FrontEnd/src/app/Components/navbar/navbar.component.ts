import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  constructor(private router: Router){}
  goToAddBooks()
  {
    this.router.navigate(['/addbooks']);
  }
  goToDeleteBooks()
  {
    this.router.navigate(['/deletebooks']);
  }
  goToSearchBooks()
  {
    this.router.navigate(['/searchbooks']);
  }
  goToReturnBooks()
  {
    this.router.navigate(['/returnbooks']);
  }
  goToReports()
  {
    this.router.navigate(['/reports']);
  }
}
