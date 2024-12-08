import { Component } from '@angular/core'; 
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(private router: Router, private userService: UserService) {}

  goToRegistration() {
    this.router.navigate(['/register']);
  }

  email: string = '';
  password: string = '';

  login() {
    this.userService.loginUser(this.email, this.password).subscribe({
      next: (response: any) => {
        alert('Login successful!');
        
        this.userService.setUserEmail(this.email);

        this.userService.isAdmin(this.email).subscribe({
          next: (isAdmin: boolean) => {
            if (isAdmin) {
              this.router.navigate(['/main']);
            } else {
              this.router.navigate(['/user']);
            }
          },
          error: (error) => {
            alert('Error checking user role!');
            console.error('isAdmin error:', error);
          },
        });
      },
      error: (error) => {
        alert('Invalid credentials!');
        console.error('Login error:', error);
      },
      complete: () => {
        console.log('Login process completed.');
      },
    });
  }
}
