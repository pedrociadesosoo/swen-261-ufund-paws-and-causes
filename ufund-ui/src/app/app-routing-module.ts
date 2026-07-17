import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';
import { NeedDetail } from './need-detail/need-detail';
import { Login } from './login/login';
import { FundingbasketComponent } from './fundingbasket.component/fundingbasket.component';
import { Checkout } from './checkout/checkout';
import { Proposals } from './proposals/proposals';
import { authGuard, managerGuard, helperGuard, unsavedChangesGuard } from './route-guards';

const routes: Routes = [
  { path: 'cupboard', component: Cupboard, canActivate: [authGuard] },
  { path: 'cupboard/:id', component: NeedDetail, canActivate: [authGuard] },
  { path: 'add-need', component: AddNeed, canActivate: [managerGuard], canDeactivate: [unsavedChangesGuard] },
  { path: 'edit-need/:id', component: AddNeed, canActivate: [managerGuard], canDeactivate: [unsavedChangesGuard] },
  { path: 'login', component: Login },
  { path: 'basket', component: FundingbasketComponent, canActivate: [helperGuard] },
  { path: 'checkout/:id', component: Checkout, canActivate: [helperGuard] },
  { path: 'proposals', component: Proposals, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
