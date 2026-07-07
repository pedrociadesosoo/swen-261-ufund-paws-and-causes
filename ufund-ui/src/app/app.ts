import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from './account';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ufund-ui');

  constructor(private accountService: AccountService, private router: Router) {}

  /**
   * True if a user is currently logged in. Used by the nav bar to switch
   * between showing a "Login" link and a "Logout" button.
   */
  isLoggedIn(): boolean {
    return this.accountService.isLoggedIn();
  }

  /**
   * Logs the current user out and returns them to the login page.
   */
  logout(): void {
    this.accountService.logout();
    this.router.navigate(['/login']);
  }
}
