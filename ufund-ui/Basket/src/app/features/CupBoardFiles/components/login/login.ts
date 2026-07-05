import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AccountStateService } from '../../../frontEnd/services/account-state.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <div class="wrap">
      <div class="card">
        <h1>Sign in to UFund</h1>
        <p class="hint">
          Use <code>admin</code> / <code>admin</code> for admin access,
          or any other credentials for a Helper account.
        </p>

        @if (error()) {
          <div class="err">{{ error() }}</div>
        }

        <label class="field">
          <span>Username</span>
          <input [(ngModel)]="username" type="text" placeholder="username"
                 (keyup.enter)="login()" autofocus />
        </label>

        <label class="field">
          <span>Password</span>
          <input [(ngModel)]="password" type="password" placeholder="password"
                 (keyup.enter)="login()" />
        </label>

        <button class="btn-primary" (click)="login()">Sign in</button>

        <a routerLink="/needs" class="skip">Continue without signing in →</a>
      </div>
    </div>
  `,
  styles: [`
    .wrap {
      display: flex; justify-content: center;
      align-items: center; min-height: 65vh;
    }
    .card {
      width: 100%; max-width: 390px;
      background: #161b22; border: 1px solid #21262d;
      border-radius: 10px; padding: 2rem;
    }
    h1 { font-family: Georgia, serif; font-size: 1.55rem; margin: 0 0 .5rem; color: #e6edf3; }
    .hint { font-size: .8rem; color: #8b949e; margin: 0 0 1.5rem; line-height: 1.5; }
    code { background: #0d1117; padding: 1px 5px; border-radius: 3px; }
    .err {
      background: rgba(248,81,73,.1); border: 1px solid rgba(248,81,73,.4);
      color: #f85149; padding: .55rem .8rem; border-radius: 6px;
      font-size: .85rem; margin-bottom: 1rem;
    }
    .field {
      display: flex; flex-direction: column; gap: .35rem;
      margin-bottom: 1rem;
    }
    .field span {
      font-size: .72rem; font-weight: 600;
      text-transform: uppercase; letter-spacing: .07em; color: #8b949e;
    }
    .field input {
      background: #0d1117; border: 1px solid #21262d;
      color: #e6edf3; padding: .6rem .75rem; border-radius: 6px;
      font-size: .92rem; outline: none; transition: border-color .15s;
    }
    .field input:focus { border-color: #388bfd; }
    .btn-primary {
      width: 100%; background: #238636; border: 1px solid #2ea043;
      color: #fff; padding: .65rem; border-radius: 6px;
      font-size: .95rem; font-weight: 600; cursor: pointer;
      transition: background .15s; margin-top: .25rem;
    }
    .btn-primary:hover { background: #2ea043; }
    .skip {
      display: block; text-align: center; margin-top: 1rem;
      font-size: .8rem; color: #8b949e; text-decoration: none;
    }
    .skip:hover { color: #e6edf3; }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  error = signal('');

  constructor(private acc: AccountStateService, private router: Router) {}

  login() {
    if (!this.username.trim() || !this.password.trim()) {
      this.error.set('Please enter username and password.');
      return;
    }
    this.acc.login(this.username.trim(), this.password);
    this.router.navigate(['/needs']);
  }
}
