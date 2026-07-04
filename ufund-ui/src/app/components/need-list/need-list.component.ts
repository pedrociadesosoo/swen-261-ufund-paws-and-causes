import { Component, OnInit, OnDestroy, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Subject, Subscription, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError } from 'rxjs/operators';
import { NeedService } from '../../services/need.service';
import { AccountStateService } from '../../services/account-state.service';
import { Need } from '../../models/need.model';

@Component({
  selector: 'app-need-list',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <!-- Page header -->
    <div class="page-head">
      <h1>Cupboard</h1>
      <p class="sub">Fund the community's needs.</p>
    </div>

    <!-- Search -->
    <div class="search-wrap">
      <div class="search-box" [class.focused]="searchFocused">
        <svg class="s-icon" viewBox="0 0 20 20" fill="none">
          <circle cx="9" cy="9" r="6" stroke="#8b949e" stroke-width="1.5"/>
          <path d="M13.5 13.5L17 17" stroke="#8b949e" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        <input
          type="text"
          [(ngModel)]="searchTerm"
          (ngModelChange)="onSearch($event)"
          (focus)="searchFocused=true"
          (blur)="searchFocused=false"
          placeholder="Search needs by name…"
          class="s-input"
        />
        @if (searchTerm) {
          <button class="s-clear" (click)="clear()">✕</button>
        }
      </div>

      <!-- History pills -->
      @if (history().length > 0 && !searchTerm) {
        <div class="history">
          <span class="hist-label">Recent:</span>
          @for (t of history(); track t) {
            <button class="pill" (click)="applyHistory(t)">{{ t }}</button>
          }
        </div>
      }
    </div>

    <!-- State feedback -->
    @if (loading()) {
      <div class="feedback">Loading…</div>
    } @else if (apiError()) {
      <div class="feedback error">
        Cannot reach the API at <code>localhost:8080</code>. Make sure the Spring Boot server is running.
      </div>
    } @else if (needs().length === 0) {
      <div class="feedback">No needs found{{ searchTerm ? ' for "' + searchTerm + '"' : '' }}.</div>
    } @else {
      <p class="count">{{ needs().length }} result{{ needs().length !== 1 ? 's' : '' }}</p>
      <div class="grid">
        @for (n of needs(); track n.id) {
          <div class="card" [class.in-basket]="inBasket(n.id)">
            <span class="type">{{ n.type }}</span>
            <h3 class="name">{{ n.name }}</h3>
            <div class="meta">
              <span class="cost">\${{ n.cost.toFixed(2) }}</span>
              <span class="qty">× {{ n.quantity }}</span>
            </div>
            <div class="card-foot">
              <a [routerLink]="['/needs', n.id]" class="btn-details">Details</a>
              @if (isLoggedIn()) {
                @if (inBasket(n.id)) {
                  <button class="btn-remove" (click)="removeFromBasket(n.id)">Remove</button>
                } @else {
                  <button class="btn-fund" (click)="addToBasket(n.id)">Fund this</button>
                }
              } @else {
                <a routerLink="/login" class="btn-fund-ghost">Sign in to fund</a>
              }
            </div>
          </div>
        }
      </div>
    }
  `,
  styles: [`
    .page-head { margin-bottom: 1.5rem; }
    h1 { font-family: Georgia, serif; font-size: 2.2rem; margin: 0 0 .2rem; color: #e6edf3; }
    .sub { margin: 0; color: #8b949e; font-size: .95rem; }

    /* Search */
    .search-wrap { margin-bottom: 1.25rem; }
    .search-box {
      display: flex; align-items: center;
      background: #161b22; border: 1px solid #21262d;
      border-radius: 8px; padding: 0 .85rem; gap: .55rem;
      max-width: 500px; transition: border-color .15s;
    }
    .search-box.focused { border-color: #388bfd; }
    .s-icon { width: 16px; height: 16px; flex-shrink: 0; }
    .s-input {
      flex: 1; background: none; border: none; outline: none;
      color: #e6edf3; padding: .7rem 0; font-size: .93rem;
    }
    .s-input::placeholder { color: #4d5566; }
    .s-clear {
      background: none; border: none; color: #8b949e;
      cursor: pointer; font-size: .75rem; padding: 2px 4px; line-height: 1;
    }
    .s-clear:hover { color: #e6edf3; }

    .history { display: flex; align-items: center; gap: .5rem; flex-wrap: wrap; margin-top: .6rem; }
    .hist-label { font-size: .72rem; color: #8b949e; text-transform: uppercase; letter-spacing: .06em; }
    .pill {
      background: #0d1117; border: 1px solid #21262d;
      color: #8b949e; padding: .2rem .65rem; border-radius: 99px;
      font-size: .75rem; cursor: pointer; transition: color .15s, border-color .15s;
    }
    .pill:hover { color: #e6edf3; border-color: #8b949e; }

    /* Feedback */
    .feedback { color: #8b949e; text-align: center; padding: 4rem 0; font-size: .95rem; }
    .feedback.error {
      color: #f85149; background: rgba(248,81,73,.07);
      border: 1px solid rgba(248,81,73,.2); border-radius: 8px;
      padding: 1rem 1.25rem; text-align: left;
    }
    .feedback.error code { background: #0d1117; padding: 1px 5px; border-radius: 3px; }

    .count { font-size: .78rem; color: #8b949e; margin: 0 0 1rem; }

    /* Grid */
    .grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
      gap: 1rem;
    }
    .card {
      background: #161b22; border: 1px solid #21262d;
      border-radius: 10px; padding: 1.2rem;
      display: flex; flex-direction: column; gap: .55rem;
      transition: border-color .15s;
    }
    .card:hover { border-color: #30363d; }
    .card.in-basket { border-color: #39d353; }

    .type {
      font-size: .65rem; font-weight: 700;
      text-transform: uppercase; letter-spacing: .1em; color: #39d353;
    }
    .name { margin: 0; font-size: 1rem; color: #e6edf3; }
    .meta { display: flex; justify-content: space-between; align-items: baseline; }
    .cost { font-family: Georgia, serif; font-size: 1.15rem; font-weight: 700; color: #e6edf3; }
    .qty { font-size: .78rem; color: #8b949e; }

    .card-foot { display: flex; gap: .5rem; margin-top: auto; }
    .btn-details {
      flex: 1; text-align: center; text-decoration: none;
      border: 1px solid #21262d; color: #8b949e;
      padding: .38rem 0; border-radius: 6px; font-size: .8rem;
      transition: color .15s, border-color .15s;
    }
    .btn-details:hover { color: #e6edf3; border-color: #8b949e; }
    .btn-fund {
      flex: 1; background: #238636; border: 1px solid #2ea043;
      color: #fff; padding: .38rem 0; border-radius: 6px;
      font-size: .8rem; font-weight: 600; cursor: pointer; transition: background .15s;
    }
    .btn-fund:hover { background: #2ea043; }
    .btn-remove {
      flex: 1; background: none; border: 1px solid #39d353;
      color: #39d353; padding: .38rem 0; border-radius: 6px;
      font-size: .8rem; font-weight: 600; cursor: pointer;
    }
    .btn-fund-ghost {
      flex: 1; text-align: center; text-decoration: none;
      border: 1px solid #21262d; color: #8b949e;
      padding: .38rem 0; border-radius: 6px; font-size: .75rem;
    }
  `]
})
export class NeedListComponent implements OnInit, OnDestroy {
  needs      = signal<Need[]>([]);
  loading    = signal(true);
  apiError   = signal(false);
  searchTerm = '';
  searchFocused = false;

  history    = this.acc.searchHistory;
  isLoggedIn = this.acc.isLoggedIn;
  inBasket   = (id: number) => this.acc.basket().includes(id);

  private search$ = new Subject<string>();
  private sub!: Subscription;

  constructor(private svc: NeedService, private acc: AccountStateService) {}

  ngOnInit(): void {
    // Restore last search term from saved history
    const restored = this.acc.searchHistory()[0] ?? '';
    this.searchTerm = restored;

    this.sub = this.search$
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => {
          this.loading.set(true);
          this.apiError.set(false);
          const req$ = term.trim()
            ? (this.acc.recordSearch(term.trim()), this.svc.search(term.trim()))
            : this.svc.getAll();
          return req$.pipe(
            catchError(() => {
              this.apiError.set(true);
              this.loading.set(false);
              return of([]);
            })
          );
        })
      )
      .subscribe(needs => {
        this.needs.set(needs);
        this.loading.set(false);
      });

    // Fire initial load with restored search
    this.search$.next(this.searchTerm);
  }

  ngOnDestroy(): void { this.sub?.unsubscribe(); }

  onSearch(term: string) { this.search$.next(term); }

  clear() {
    this.searchTerm = '';
    this.search$.next('');
  }

  applyHistory(term: string) {
    this.searchTerm = term;
    this.search$.next(term);
  }

  addToBasket(id: number)    { this.acc.addToBasket(id); }
  removeFromBasket(id: number) { this.acc.removeFromBasket(id); }
}
