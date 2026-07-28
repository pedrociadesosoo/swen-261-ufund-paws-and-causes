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
   * True if the logged-in user is the manager. Used by the nav bar to hide
   * "Add Need" from helpers.
   */
  isManager(): boolean {
    return this.accountService.isManager();
  }

  /**
   * True if the logged-in user is a helper. Used by the nav bar to hide
   * "My Basket" from the manager.
   */
  isHelper(): boolean {
    return this.accountService.isHelper();
  }

  /**
   * The logged-in user's username, or '' if no one is logged in. Used by the
   * nav bar so people can see who they're signed in as.
   */
  currentUsername(): string {
    return this.accountService.getCurrentAccount()?.username ?? '';
  }

  /**
   * A label for the logged-in user's account type
   */
  currentAccountTypeLabel(): string {
    return this.isManager() ? 'Manager' : 'Helper';
  }

  /**
   * Logs the current user out and returns them to the login page.
   */
  logout(): void {
    this.router.navigate(['/login']).then(navigated => {
      if (navigated) {
        this.accountService.logout();
      }
    });
  }
}
