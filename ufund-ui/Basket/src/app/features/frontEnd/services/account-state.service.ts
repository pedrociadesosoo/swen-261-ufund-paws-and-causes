import { Injectable, signal, computed } from '@angular/core';

export type UserRole = 'helper' | 'admin' | null;

export interface AccountState {
  username: string | null;
  role: UserRole;
  basket: number[];
  searchHistory: string[];
}

const KEY = 'ufund_acc';

const defaults: AccountState = {
  username: null,
  role: null,
  basket: [],
  searchHistory: [],
};

@Injectable({ providedIn: 'root' })
export class AccountStateService {
  private _state = signal<AccountState>(this.load());

  readonly state       = this._state.asReadonly();
  readonly isLoggedIn  = computed(() => this._state().username !== null);
  readonly isAdmin     = computed(() => this._state().role === 'admin');
  readonly username    = computed(() => this._state().username);
  readonly role        = computed(() => this._state().role);
  readonly basket      = computed(() => this._state().basket);
  readonly searchHistory = computed(() => this._state().searchHistory);

  /** Returns true always; role derived from credentials */
  login(username: string, password: string): boolean {
    const role: UserRole =
      username === 'admin' && password === 'admin' ? 'admin' : 'helper';
    this.patch({ username, role });
    return true;
  }

  logout(): void {
    this.patch({ username: null, role: null, basket: [], searchHistory: [] });
  }

  addToBasket(id: number): void {
    if (!this._state().basket.includes(id))
      this.patch({ basket: [...this._state().basket, id] });
  }

  removeFromBasket(id: number): void {
    this.patch({ basket: this._state().basket.filter(x => x !== id) });
  }

  clearBasket(): void {
    this.patch({ basket: [] });
  }

  /** Saves term; deduplicates; keeps last 5 */
  recordSearch(term: string): void {
    if (!term.trim()) return;
    const hist = [
      term,
      ...this._state().searchHistory.filter(t => t !== term),
    ].slice(0, 5);
    this.patch({ searchHistory: hist });
  }

  private patch(partial: Partial<AccountState>): void {
    const next = { ...this._state(), ...partial };
    this._state.set(next);
    try { localStorage.setItem(KEY, JSON.stringify(next)); } catch {}
  }

  private load(): AccountState {
    try {
      const raw = localStorage.getItem(KEY);
      if (raw) return { ...defaults, ...JSON.parse(raw) };
    } catch {}
    return { ...defaults };
  }
}
