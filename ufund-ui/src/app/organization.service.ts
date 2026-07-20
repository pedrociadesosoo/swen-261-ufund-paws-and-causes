import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Organization } from './organization';

@Injectable({providedIn: 'root',})
export class OrganizationService {
  private readonly API = 'http://localhost:8080/organization';

  constructor(private http: HttpClient) { }

  getOrganization(name: string): Observable<Organization> {
    return this.http.get<Organization>(`${this.API}/${name}`);
  }

  getOrganizationArray(): Observable<Organization[]> {
    return this.http.get<Organization[]>(`${this.API}`);
  }

  createOrganization(o: Organization): Observable<Organization>{
    return this.http.post<Organization>(this.API, o);
  }

  updateOrganization(o: Organization): Observable<Organization>{
    return this.http.post<Organization>(this.API, o);
  }

  deleteOrganization(name: string): Observable<boolean>{
    return this.http.delete<boolean>(`${this.API}/${name}`);
  }

  addNeed(name: string, id: number): Observable<boolean>{
    return this.http.post<boolean>(`${this.API}/${name}/${id}`, null);
  }

  deleteNeed(name: string, id: number): Observable<Boolean>{
    return this.http.delete<boolean>(`${this.API}/${name}/${id}`);
  }
}
