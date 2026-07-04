import { Component, computed } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AccountStateService } from './services/account-state.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header class="nav">
      <div class="nav-inner">
        <a routerLink="/needs" class="brand">
          <span class="brand-dot">◎</span> UFund
        </a>

        <nav class="links">
          <a routerLink="/needs" routerLinkActive="active" [routerLinkActiveOptions]="{exact:false}">Cupboard</a>
          @if (isAdmin()) {
            <a routerLink="/admin" routerLinkActive="active">Admin</a>
          }
          @if (isLoggedIn()) {
            <a routerLink="/basket" routerLinkActive="active" class="basket-link">
              Basket
              @if (basketCount() > 0) {
                <span class="badge">{{ basketCount() }}</span>
              }
            </a>
          }
        </nav>

        <div class="nav-end">
          @if (isLoggedIn()) {
            <span class="user-label">{{ username() }}</span>
            <button class="btn-out" (click)="logout()">Sign out</button>
          } @else {
            <a routerLink="/login" class="btn-signin">Sign in</a>
          }
        </div>
      </div>
    </header>

    <main class="page">
      <router-outlet />
    </main>
  `,
  styles: [`
    .nav {
      position: sticky; top: 0; z-index: 100;
      background: #0d1117; border-bottom: 1px solid #21262d;
    }
    .nav-inner {
      max-width: 1100px; margin: 0 auto;
      padding: 0 1.5rem; height: 58px;
      display: flex; align-items: center; gap: 2rem;
    }
    .brand {
      display: flex; align-items: center; gap: .45rem;
      font-weight: 700; font-size: 1.1rem; font-family: Georgia, serif;
      color: #e6edf3; text-decoration: none; letter-spacing: -.01em;
    }
    .brand-dot { color: #39d353; font-size: 1.3rem; }

    .links { display: flex; gap: 1.5rem; flex: 1; }
    .links a {
      color: #8b949e; text-decoration: none;
      font-size: .83rem; font-weight: 500;
      letter-spacing: .04em; text-transform: uppercase;
      transition: color .15s;
    }
    .links a:hover, .links a.active { color: #e6edf3; }

    .basket-link { display: flex; align-items: center; gap: .35rem; }
    .badge {
      background: #39d353; color: #0d1117;
      font-size: .65rem; font-weight: 800;
      padding: 1px 5px; border-radius: 99px;
    }

    .nav-end { display: flex; align-items: center; gap: .9rem; margin-left: auto; }
    .user-label { font-size: .82rem; color: #8b949e; }
    .btn-signin {
      background: #238636; color: #fff;
      padding: .35rem .9rem; border-radius: 6px;
      text-decoration: none; font-size: .82rem; font-weight: 600;
      border: 1px solid #2ea043;
    }
    .btn-out {
      background: none; border: 1px solid #21262d;
      color: #8b949e; padding: .35rem .9rem; border-radius: 6px;
      cursor: pointer; font-size: .82rem; transition: color .15s, border-color .15s;
    }
    .btn-out:hover { color: #e6edf3; border-color: #8b949e; }

    .page { max-width: 1100px; margin: 0 auto; padding: 2rem 1.5rem; }
  `]
})
export class AppComponent {
  isLoggedIn  = this.acc.isLoggedIn;
  isAdmin     = this.acc.isAdmin;
  username    = this.acc.username;
  basketCount = computed(() => this.acc.basket().length);

  constructor(private acc: AccountStateService) {}

  logout() { this.acc.logout(); }
}
