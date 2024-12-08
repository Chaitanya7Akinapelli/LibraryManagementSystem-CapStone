import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BorrowingService {
  private baseUrl = 'http://localhost:8095/api/borrow';

  constructor(private http: HttpClient) {}

  getAvailableBooks(userEmail: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/availableBooks`, {
      params: { userEmail },
    });
  }

  getBorrowedBooks(userEmail: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/borrowedBooks`, {
      params: { userEmail },
    });
  }

  borrowBook(userEmail: string, bookIsbn: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/borrowBook`, {}, {
      params: { userEmail, bookIsbn },
      responseType: 'text',
    });
  }

  sendOverdueNotifications(userEmail : string): Observable<any> {
    return this.http.post(`${this.baseUrl}/sendOverdueNotifications`, {}, {
      params: { userEmail},
      responseType: 'text',
    });
  }
}
