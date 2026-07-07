import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from './account.model';
@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8080/accounts';
  public currentAccount: Account | null  = null;
  constructor(private http: HttpClient) {}

  /**
   * sends a request to log in as a given username
   */
  login(username: string): Observable<Account> {
      return this.http.post<Account>(`${this.apiUrl}/login`, { username });
  }

  createUser(username: string): Observable<Account> {
  	return this.http.post<Account>(`${this.apiUrl}/register`, {username});
  }
  setCurrentAccount(account: Account): void {
  this.currentAccount = account;
  }
  getCurrentAccount(): Account | null {
  return this.currentAccount;
  }

  
}
