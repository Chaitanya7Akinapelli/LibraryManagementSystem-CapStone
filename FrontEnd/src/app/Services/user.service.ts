import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8095/api/users';
  private reportsUrl = 'http://localhost:8095/admin/reports';

  private userEmail: string | null = null;

  setUserEmail(email: string) {
    this.userEmail = email;
  }

  getUserEmail(): string | null {
    return this.userEmail;
  }

  constructor(private http: HttpClient) {}

  registerUser(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  loginUser(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, { email, password }).pipe(
      tap(response => {
        if (response && response.email) {
          this.setUserEmail(response.email);
        }
      })
    );
  }

  isAdmin(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/isAdmin`, { params: { email } });
  }

  getBorrowedBooks(userEmail: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/admin/borrowedBooks`, { params: { userEmail } });
  }

  returnBookByAdmin(userEmail: string, bookIsbn: string, adminEmail: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/admin/returnBook`, null, {
      params: { userEmail, bookIsbn, adminEmail }
    });
  }
  
  getAllReports(): Observable<any[]> {
    return this.http.get<any[]>(`${this.reportsUrl}/generate`);
  }

  getKpi(): Observable<any> {
    return this.http.get<any>(`${this.reportsUrl}/kpi`);
  }

  getAdditionalMetrics(): Observable<any> {
    return this.http.get<any>(`${this.reportsUrl}/additional-metrics`);
  }
}
