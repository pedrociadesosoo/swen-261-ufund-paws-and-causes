import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Need } from './need.model';
import { NeedType } from './need-type';

@Injectable({
  providedIn: 'root'
})
export class NeedService {
  private apiUrl = 'http://localhost:8080/needs';

  constructor(private http: HttpClient) {}

  /**
   * Gets all needs or searches by partial name
   */
  getNeeds(name?: string, type?: NeedType): Observable<Need[]> {
    if (type && name) {
      //TODO: make sure this is how to call it with both name and type params
      return this.http.get<Need[]>(`${this.apiUrl}?name=${name}&type=${type}`);
    } else {
      if(type)
        return this.http.get<Need[]>(`${this.apiUrl}?type=${type}`);
      else if(name)
        return this.http.get<Need[]>(`${this.apiUrl}?name=${name}`);
      else 
        return this.http.get<Need[]>(this.apiUrl);
    } 
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

  /**
   * Updates an existing need
   */
  updateNeed(id: number, need: Need): Observable<Need> {
    return this.http.put<Need>(`${this.apiUrl}/${id}`, need);
  }

  /**
   * Deletes a need by id
   */
  deleteNeed(id: number): Observable<Need> {
    return this.http.delete<Need>(`${this.apiUrl}/${id}`);
  }

}