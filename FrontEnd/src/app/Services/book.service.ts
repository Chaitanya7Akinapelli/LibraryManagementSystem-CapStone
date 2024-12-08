import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private baseUrl = 'http://localhost:8095/api/books';

  constructor(private http: HttpClient) {}

  getBooks(page: number, size: number): Observable<any> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http.get(`${this.baseUrl}/page`, { params });
  }

  searchBooks(query: { title?: string; author?: string; genre?: string}, userEmail: string, page: number, size: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/search`, {
      params: {
        ...query, // Spread query parameters (title, author, genre)
        userEmail, // Send the user email
        page: page.toString(),
        size: size.toString(),
      },
    });
  }
  

  getSearchLogs(query: { title?: string; author?: string; genre?: string }, userEmail: string, resultsCount: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/searchLogs`, { ...query, userEmail, resultsCount });
  }

  addBook(book: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/add`, book);
  }

  deleteBook(bookId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${bookId}`);
  }

  checkAdminRole(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/isAdmin?email=${email}`);
  }
}
