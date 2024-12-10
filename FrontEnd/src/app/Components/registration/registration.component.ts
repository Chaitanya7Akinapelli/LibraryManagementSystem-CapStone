import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [FormsModule , CommonModule],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  constructor(private router: Router , private userService: UserService) {}

  goToLogin() {
    this.router.navigate(['/login']);
  }

  user = {
    name: '',
    email: '',
    phoneNumber: '',
    password: '',
    confirmPassword: '',
    role: '',
  };

  register() {
    if (this.user.password !== this.user.confirmPassword) {
      alert('Passwords do not match!');
      return;
    }
  
    const { confirmPassword, ...userData } = this.user;
  
    this.userService.registerUser(this.user).subscribe({
      next: (response) => {
        alert('Registration successful!');
      },
      error: (error) => {
        alert('Registration failed!');
      },
      complete: () => {
        console.log('Registration process completed.');
      },
    });
  }  
}