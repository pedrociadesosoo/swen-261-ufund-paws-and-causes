import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface Account {
  username: string;
  type: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/accounts';
  private currentAccount: Account | null = null;

  constructor(private http: HttpClient) {}

  /**
   * Logs in a user with the given username
   */
  login(username: string): Observable<Account> {
    return this.http.post<Account>(`${this.apiUrl}/login`, { username }).pipe(
      tap(account => this.currentAccount = account)
    );
  }

  /**
   * Logs out the current user
   */
  logout(): void {
    this.currentAccount = null;
  }

  /**
   * Returns the currently logged in account
   */
  getCurrentAccount(): Account | null {
    return this.currentAccount;
  }

  /**
   * Returns true if the current user is a manager/admin
   */
  isManager(): boolean {
    return this.currentAccount?.type === 'admin';
  }

  /**
   * Returns true if the current user is a helper
   */
  isHelper(): boolean {
    return this.currentAccount?.type === 'helper';
  }

  /**
   * Returns true if any user is logged in
   */
  isLoggedIn(): boolean {
    return this.currentAccount !== null;
  }
}