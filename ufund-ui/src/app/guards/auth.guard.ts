import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AccountStateService } from '../services/account-state.service';

export const authGuard: CanActivateFn = () => {
  const acc = inject(AccountStateService);
  const router = inject(Router);
  return acc.isLoggedIn() ? true : router.createUrlTree(['/login']);
};

export const adminGuard: CanActivateFn = () => {
  const acc = inject(AccountStateService);
  const router = inject(Router);
  return acc.isAdmin() ? true : router.createUrlTree(['/needs']);
};
