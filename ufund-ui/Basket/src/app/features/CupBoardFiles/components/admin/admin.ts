import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NeedService } from '../../../frontEnd/services/need.service';
import { Need } from '../../../frontEnd/models/need.model';

type Mode = 'create' | 'edit' | null;

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="head">
      <h1>Admin — Needs</h1>
      <button class="btn-new" (click)="openCreate()">+ New need</button>
    </div>

    <!-- Inline form -->
    @if (mode()) {
      <div class="form-panel">
        <h2>{{ mode() === 'create' ? 'Create need' : 'Edit need #' + editId }}</h2>
        <div class="fg">
          <label class="f">
            <span>Name *</span>
            <input [(ngModel)]="f.name" placeholder="e.g. Winter Coats" />
          </label>
          <label class="f">
            <span>Type *</span>
            <input [(ngModel)]="f.type" placeholder="e.g. clothing" />
          </label>
          <label class="f">
            <span>Cost ($) *</span>
            <input [(ngModel)]="f.cost" type="number" min="0" step="0.01" />
          </label>
          <label class="f">
            <span>Quantity *</span>
            <input [(ngModel)]="f.quantity" type="number" min="0" />
          </label>
        </div>
        @if (formErr()) { <p class="ferr">{{ formErr() }}</p> }
        <div class="form-btns">
          <button class="btn-save" (click)="save()">
            {{ mode() === 'create' ? 'Create' : 'Save changes' }}
          </button>
          <button class="btn-cancel" (click)="closeForm()">Cancel</button>
        </div>
      </div>
    }

    <!-- Table -->
    @if (loading()) {
      <p class="feedback">Loading…</p>
    } @else if (needs().length === 0) {
      <p class="feedback">No needs in the cupboard yet.</p>
    } @else {
      <div class="tbl-wrap">
        <table>
          <thead>
            <tr>
              <th>ID</th><th>Name</th><th>Type</th><th>Cost</th><th>Qty</th><th></th>
            </tr>
          </thead>
          <tbody>
            @for (n of needs(); track n.id) {
              <tr>
                <td class="muted">#{{ n.id }}</td>
                <td class="bold">{{ n.name }}</td>
                <td><span class="type-tag">{{ n.type }}</span></td>
                <td>\${{ n.cost.toFixed(2) }}</td>
                <td>{{ n.quantity }}</td>
                <td class="actions">
                  <button class="btn-edit" (click)="openEdit(n)">Edit</button>
                  <button class="btn-del" (click)="del(n.id)">Delete</button>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    }
  `,
  styles: [`
    .head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
    h1 { font-family: Georgia, serif; font-size: 2rem; margin: 0; color: #e6edf3; }
    .btn-new {
      background: #238636; border: 1px solid #2ea043; color: #fff;
      padding: .5rem 1rem; border-radius: 6px; font-size: .85rem;
      font-weight: 600; cursor: pointer;
    }

    .form-panel {
      background: #161b22; border: 1px solid #388bfd;
      border-radius: 10px; padding: 1.5rem; margin-bottom: 1.5rem;
    }
    h2 { font-family: Georgia, serif; font-size: 1.15rem; margin: 0 0 1rem; color: #e6edf3; }
    .fg { display: grid; grid-template-columns: 1fr 1fr; gap: .75rem 1.25rem; margin-bottom: 1rem; }
    .f { display: flex; flex-direction: column; gap: .3rem; }
    .f span {
      font-size: .7rem; font-weight: 600;
      text-transform: uppercase; letter-spacing: .07em; color: #8b949e;
    }
    .f input {
      background: #0d1117; border: 1px solid #21262d; color: #e6edf3;
      padding: .55rem .75rem; border-radius: 6px; font-size: .9rem;
      outline: none; transition: border-color .15s;
    }
    .f input:focus { border-color: #388bfd; }
    .ferr { color: #f85149; font-size: .82rem; margin: 0 0 .75rem; }
    .form-btns { display: flex; gap: .75rem; }
    .btn-save {
      background: #238636; border: 1px solid #2ea043; color: #fff;
      padding: .5rem 1.1rem; border-radius: 6px; font-size: .85rem;
      font-weight: 600; cursor: pointer;
    }
    .btn-cancel {
      background: none; border: 1px solid #21262d; color: #8b949e;
      padding: .5rem 1.1rem; border-radius: 6px; font-size: .85rem; cursor: pointer;
    }

    .feedback { color: #8b949e; text-align: center; padding: 3rem 0; }
    .tbl-wrap { overflow-x: auto; }
    table { width: 100%; border-collapse: collapse; font-size: .875rem; }
    th {
      text-align: left; font-size: .68rem; font-weight: 600;
      text-transform: uppercase; letter-spacing: .07em; color: #8b949e;
      border-bottom: 1px solid #21262d; padding: .5rem .75rem;
    }
    td { padding: .75rem; border-bottom: 1px solid #21262d; color: #c9d1d9; }
    tr:last-child td { border-bottom: none; }
    .muted { color: #8b949e; font-family: monospace; }
    .bold { color: #e6edf3; font-weight: 500; }
    .type-tag {
      font-size: .62rem; font-weight: 700; text-transform: uppercase;
      letter-spacing: .1em; color: #39d353;
    }
    .actions { display: flex; gap: .5rem; justify-content: flex-end; }
    .btn-edit {
      background: none; border: 1px solid #21262d; color: #8b949e;
      padding: .28rem .65rem; border-radius: 5px; font-size: .75rem; cursor: pointer;
    }
    .btn-edit:hover { color: #e6edf3; border-color: #8b949e; }
    .btn-del {
      background: none; border: 1px solid transparent; color: #8b949e;
      padding: .28rem .65rem; border-radius: 5px; font-size: .75rem; cursor: pointer;
    }
    .btn-del:hover { color: #f85149; border-color: #f85149; }
  `]
})
export class AdminComponent implements OnInit {
  needs   = signal<Need[]>([]);
  loading = signal(true);
  mode    = signal<Mode>(null);
  formErr = signal('');
  editId: number | null = null;

  f = { name: '', type: '', cost: 0, quantity: 0 };

  constructor(private svc: NeedService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading.set(true);
    this.svc.getAll().subscribe({
      next: ns => { this.needs.set(ns); this.loading.set(false); },
      error: ()  => this.loading.set(false),
    });
  }

  openCreate() {
    this.mode.set('create'); this.editId = null;
    this.f = { name: '', type: '', cost: 0, quantity: 0 };
    this.formErr.set('');
  }

  openEdit(n: Need) {
    this.mode.set('edit'); this.editId = n.id;
    this.f = { name: n.name, type: n.type, cost: n.cost, quantity: n.quantity };
    this.formErr.set('');
  }

  closeForm() { this.mode.set(null); }

  save() {
    if (!this.f.name.trim() || !this.f.type.trim()) {
      this.formErr.set('Name and Type are required.'); return;
    }
    if (this.f.cost < 0 || this.f.quantity < 0) {
      this.formErr.set('Cost and Quantity must be non-negative.'); return;
    }

    if (this.mode() === 'create') {
      this.svc.create(this.f).subscribe({
        next: () => { this.closeForm(); this.load(); },
        error: ()  => this.formErr.set('Failed to create need. Is the API running?'),
      });
    } else if (this.editId !== null) {
      this.svc.update(this.editId, { id: this.editId, ...this.f }).subscribe({
        next: () => { this.closeForm(); this.load(); },
        error: ()  => this.formErr.set('Failed to update need.'),
      });
    }
  }

  del(id: number) {
    if (!confirm('Delete this need?')) return;
    this.svc.delete(id).subscribe({
      next: () => this.needs.update(ns => ns.filter(n => n.id !== id)),
      error: ()  => alert('Delete failed.'),
    });
  }
}
