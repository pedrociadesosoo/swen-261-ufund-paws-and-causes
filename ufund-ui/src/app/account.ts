import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from './account.model';
@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8080/accounts';
  private currentAccount: Account | null  = null;
  constructor(private http: HttpClient) {}

  /**
   * sends a request to log in as a given username
   */
  login(username: string, password: string): Observable<Account> {
      return this.http.post<Account>(`${this.apiUrl}/login`, { username, password });
  }

  createUser(username: string, password: string): Observable<Account> {
  	return this.http.post<Account>(`${this.apiUrl}/register`, {username, password});
  }
  setCurrentAccount(account: Account): void {
  this.currentAccount = account;
  }
  getCurrentAccount(): Account | null {
  return this.currentAccount;
  }

  /**
   * logs out the current user
   */
  logout(): void {
  this.currentAccount = null;
  }

  /**
   * true if the logged-in user is the manager
   */
  isManager(): boolean {
  return this.currentAccount?.type === 'admin';
  }

  /**
   * true if the logged-in user is a helper
   */
  isHelper(): boolean {
  return this.currentAccount?.type === 'helper';
  }

  /**
   * true if anyone is logged in
   */
  isLoggedIn(): boolean {
  return this.currentAccount !== null;
  }
}
