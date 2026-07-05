import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Need } from '../models/need.model';

@Injectable({ providedIn: 'root' })
export class NeedService {
  private readonly API = 'http://localhost:8080/needs';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Need[]> {
    return this.http.get<Need[]>(this.API);
  }

  search(term: string): Observable<Need[]> {
    return this.http.get<Need[]>(this.API, {
      params: new HttpParams().set('name', term)
    });
  }

  getById(id: number): Observable<Need> {
    return this.http.get<Need>(`${this.API}/${id}`);
  }

  create(need: Omit<Need, 'id'>): Observable<Need> {
    return this.http.post<Need>(this.API, need);
  }

  update(id: number, need: Need): Observable<Need> {
    return this.http.put<Need>(`${this.API}/${id}`, need);
  }

  delete(id: number): Observable<Need> {
    return this.http.delete<Need>(`${this.API}/${id}`);
  }
}
