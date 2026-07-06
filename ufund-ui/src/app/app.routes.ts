import { Routes } from '@angular/router';
import { authGuard, adminGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'needs', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./components/login/login.component').then(m => m.LoginComponent),
  },
  {
    path: 'needs',
    loadComponent: () =>
      import('./components/need-list/need-list.component').then(m => m.NeedListComponent),
  },
  {
    path: 'needs/:id',
    loadComponent: () =>
      import('./components/need-detail/need-detail.component').then(m => m.NeedDetailComponent),
    canActivate: [authGuard],
  },
  {
    path: 'basket',
    loadComponent: () =>
      import('./components/basket/basket.component').then(m => m.BasketComponent),
    canActivate: [authGuard],
  },
  {
    path: 'admin',
    loadComponent: () =>
      import('./components/admin/admin.component').then(m => m.AdminComponent),
    canActivate: [adminGuard],
  },
  { path: '**', redirectTo: 'needs' },
];
