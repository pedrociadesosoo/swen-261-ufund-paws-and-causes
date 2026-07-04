import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { NeedService } from '../../services/need.service';
import { AccountStateService } from '../../services/account-state.service';
import { Need } from '../../models/need.model';

@Component({
  selector: 'app-need-detail',
  standalone: true,
  imports: [RouterLink],
  template: `
    <a routerLink="/needs" class="back">← Back to Cupboard</a>

    @if (loading()) {
      <p class="feedback">Loading…</p>
    } @else if (!need()) {
      <p class="feedback error">Need not found.</p>
    } @else {
      <div class="detail">
        <span class="type">{{ need()!.type }}</span>
        <h1>{{ need()!.name }}</h1>

        <div class="stats">
          <div class="stat">
            <span class="stat-lbl">Cost</span>
            <span class="stat-val">\${{ need()!.cost.toFixed(2) }}</span>
          </div>
          <div class="stat">
            <span class="stat-lbl">Quantity needed</span>
            <span class="stat-val">{{ need()!.quantity }}</span>
          </div>
          <div class="stat">
            <span class="stat-lbl">ID</span>
            <span class="stat-val muted">#{{ need()!.id }}</span>
          </div>
        </div>

        <div class="actions">
          @if (inBasket()) {
            <button class="btn-remove" (click)="remove()">Remove from basket</button>
          } @else {
            <button class="btn-fund" (click)="add()">Add to basket</button>
          }
          <a routerLink="/needs" class="btn-ghost">Back</a>
        </div>
      </div>
    }
  `,
  styles: [`
    .back { color: #8b949e; text-decoration: none; font-size: .85rem; display: inline-block; margin-bottom: 1.5rem; }
    .back:hover { color: #e6edf3; }
    .feedback { color: #8b949e; text-align: center; padding: 4rem 0; }
    .feedback.error { color: #f85149; }

    .detail {
      max-width: 560px;
      background: #161b22; border: 1px solid #21262d;
      border-radius: 10px; padding: 2rem;
    }
    .type { font-size: .65rem; font-weight: 700; text-transform: uppercase; letter-spacing: .1em; color: #39d353; }
    h1 { font-family: Georgia, serif; font-size: 2rem; margin: .4rem 0 1.5rem; color: #e6edf3; }

    .stats { display: flex; gap: 2rem; flex-wrap: wrap; margin-bottom: 2rem; }
    .stat { display: flex; flex-direction: column; gap: .2rem; }
    .stat-lbl { font-size: .7rem; font-weight: 600; text-transform: uppercase; letter-spacing: .07em; color: #8b949e; }
    .stat-val { font-family: Georgia, serif; font-size: 1.5rem; font-weight: 700; color: #e6edf3; }
    .stat-val.muted { font-size: 1.1rem; color: #8b949e; font-family: monospace; }

    .actions { display: flex; gap: .75rem; }
    .btn-fund {
      background: #238636; border: 1px solid #2ea043; color: #fff;
      padding: .6rem 1.4rem; border-radius: 6px; font-size: .93rem;
      font-weight: 600; cursor: pointer; transition: background .15s;
    }
    .btn-fund:hover { background: #2ea043; }
    .btn-remove {
      background: none; border: 1px solid #39d353; color: #39d353;
      padding: .6rem 1.4rem; border-radius: 6px; font-size: .93rem;
      font-weight: 600; cursor: pointer;
    }
    .btn-ghost {
      background: none; border: 1px solid #21262d; color: #8b949e;
      padding: .6rem 1.2rem; border-radius: 6px; text-decoration: none;
      font-size: .9rem; transition: color .15s, border-color .15s;
    }
    .btn-ghost:hover { color: #e6edf3; border-color: #8b949e; }
  `]
})
export class NeedDetailComponent implements OnInit {
  need    = signal<Need | null>(null);
  loading = signal(true);

  inBasket = () => !!this.need() && this.acc.basket().includes(this.need()!.id);

  constructor(
    private route: ActivatedRoute,
    private svc: NeedService,
    private acc: AccountStateService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.svc.getById(id).subscribe({
      next: n  => { this.need.set(n); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  add()    { if (this.need()) this.acc.addToBasket(this.need()!.id); }
  remove() { if (this.need()) this.acc.removeFromBasket(this.need()!.id); }
}
