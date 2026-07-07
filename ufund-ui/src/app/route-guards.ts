import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AccountService } from './account';

/**
 * Blocks navigation to a route unless someone is currently logged in;
 * redirects anonymous visitors to the login page.
 */
export const authGuard: CanActivateFn = () => {
  const accountService = inject(AccountService);
  const router = inject(Router);
  if (accountService.isLoggedIn()) return true;
  router.navigate(['/login']);
  return false;
};

/**
 * Blocks navigation to manager-only routes (adding/editing a need) for
 * anyone who isn't logged in as the manager/admin account.
 */
export const managerGuard: CanActivateFn = () => {
  const accountService = inject(AccountService);
  const router = inject(Router);
  if (accountService.isManager()) return true;
  router.navigate(['/cupboard']);
  return false;
};

/**
 * Blocks navigation to helper-only routes (the funding basket) for anyone
 * who isn't logged in as a helper.
 */
export const helperGuard: CanActivateFn = () => {
  const accountService = inject(AccountService);
  const router = inject(Router);
  if (accountService.isHelper()) return true;
  router.navigate(['/cupboard']);
  return false;
};
