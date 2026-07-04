import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Need } from './need.model';

@Injectable({
  providedIn: 'root'
})
export class NeedService {
  private apiUrl = 'http://localhost:8080/needs';

  constructor(private http: HttpClient) {}

  /**
   * Gets all needs or searches by partial name
   */
  getNeeds(name?: string): Observable<Need[]> {
    if (name) {
      return this.http.get<Need[]>(`${this.apiUrl}?name=${name}`);
    }
    return this.http.get<Need[]>(this.apiUrl);
  }

  /**
   * Creates a new need
   */
  createNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.apiUrl, need);
  }

  /**
   * Gets a single need by id
   */
  getNeedById(id: number): Observable<Need> {
    return this.http.get<Need>(`${this.apiUrl}/${id}`);
  }
  
}