import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class UserStatisticsServiceService {
  private baseUrl = 'http://localhost:8095/api/user-statistics';

  constructor(private http: HttpClient) {}
  getUserBorrowingHistory(userEmail: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/history`, {
      params: { userEmail }
    });
  }
}
