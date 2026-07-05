import { Component, OnInit, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { NeedService } from '../../../frontEnd/services/need.service';
import { AccountStateService } from '../../../frontEnd/services/account-state.service';
import { Need } from '../../../frontEnd/models/need.model';

@Component({
  selector: 'app-basket',
  standalone: true,
  imports: [RouterLink],
  template: `
    <h1>Your Basket</h1>

    @if (loading()) {
      <p class="feedback">Loading…</p>
    } @else if (needs().length === 0) {
      <div class="empty">
        <p>Your basket is empty.</p>
        <a routerLink="/needs" class="btn-primary">Browse the Cupboard →</a>
      </div>
    } @else {
      <div class="list">
        @for (n of needs(); track n.id) {
          <div class="row">
            <div class="row-info">
              <span class="type">{{ n.type }}</span>
              <a [routerLink]="['/needs', n.id]" class="row-name">{{ n.name }}</a>
            </div>
            <span class="row-cost">\${{ n.cost.toFixed(2) }}</span>
            <button class="btn-rm" (click)="remove(n.id)">Remove</button>
          </div>
        }
      </div>

      <div class="footer">
        <div class="total">
          Total &nbsp;
          <strong>\${{ total().toFixed(2) }}</strong>
        </div>
        <button class="btn-checkout" (click)="checkout()">Confirm funding</button>
      </div>
    }
  `,
  styles: [`
    h1 { font-family: Georgia, serif; font-size: 2rem; margin: 0 0 1.5rem; color: #e6edf3; }
    .feedback { color: #8b949e; text-align: center; padding: 4rem 0; }
    .empty { text-align: center; padding: 4rem 0; }
    .empty p { color: #8b949e; margin-bottom: 1rem; }
    .btn-primary {
      background: #238636; border: 1px solid #2ea043; color: #fff;
      padding: .6rem 1.4rem; border-radius: 6px; text-decoration: none;
      font-size: .93rem; font-weight: 600;
    }

    .list { max-width: 640px; display: flex; flex-direction: column; gap: .75rem; }
    .row {
      display: flex; align-items: center; gap: 1rem;
      background: #161b22; border: 1px solid #21262d;
      border-radius: 8px; padding: .9rem 1.1rem;
    }
    .row-info { flex: 1; display: flex; flex-direction: column; gap: .15rem; }
    .type { font-size: .62rem; font-weight: 700; text-transform: uppercase; letter-spacing: .1em; color: #39d353; }
    .row-name { color: #e6edf3; text-decoration: none; font-weight: 500; font-size: .95rem; }
    .row-name:hover { text-decoration: underline; }
    .row-cost { font-family: Georgia, serif; font-size: 1.05rem; font-weight: 700; color: #e6edf3; }
    .btn-rm {
      background: none; border: 1px solid #21262d; color: #8b949e;
      padding: .3rem .7rem; border-radius: 6px; font-size: .78rem; cursor: pointer;
      transition: color .15s, border-color .15s;
    }
    .btn-rm:hover { color: #f85149; border-color: #f85149; }

    .footer {
      display: flex; align-items: center; justify-content: space-between;
      max-width: 640px; margin-top: 1.25rem;
      padding-top: 1.25rem; border-top: 1px solid #21262d;
    }
    .total { font-size: .93rem; color: #8b949e; }
    .total strong { font-family: Georgia, serif; font-size: 1.3rem; color: #e6edf3; }
    .btn-checkout {
      background: #238636; border: 1px solid #2ea043; color: #fff;
      padding: .6rem 1.4rem; border-radius: 6px;
      font-size: .93rem; font-weight: 600; cursor: pointer; transition: background .15s;
    }
    .btn-checkout:hover { background: #2ea043; }
  `]
})
export class BasketComponent implements OnInit {
  needs   = signal<Need[]>([]);
  loading = signal(true);
  total   = computed(() => this.needs().reduce((s, n) => s + n.cost, 0));

  constructor(private svc: NeedService, private acc: AccountStateService) {}

  ngOnInit(): void {
    const ids = this.acc.basket();
    if (!ids.length) { this.loading.set(false); return; }
    forkJoin(ids.map(id => this.svc.getById(id))).subscribe({
      next: needs => { this.needs.set(needs); this.loading.set(false); },
      error: ()   => this.loading.set(false),
    });
  }

  remove(id: number) {
    this.acc.removeFromBasket(id);
    this.needs.update(ns => ns.filter(n => n.id !== id));
  }

  checkout() {
    alert('Thank you! Your funding has been submitted.');
    this.acc.clearBasket();
    this.needs.set([]);
  }
}
