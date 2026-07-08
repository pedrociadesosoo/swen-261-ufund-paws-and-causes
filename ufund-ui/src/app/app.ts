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
   * Logs the current user out and returns them to the login page.
   * Navigates first and only clears the session if navigation actually
   * succeeds, so a canDeactivate guard (e.g. unsaved-changes on Edit Need)
   * can cancel the navigation and the user stays logged in.
   */
  logout(): void {
    this.router.navigate(['/login']).then(navigated => {
      if (navigated) {
        this.accountService.logout();
      }
    });
  }
}
