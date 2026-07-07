import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';
import { NeedDetail } from './need-detail/need-detail';
import { Login } from './login/login';
import { FundingbasketComponent } from './fundingbasket.component/fundingbasket.component';
import { Checkout } from './checkout/checkout';
import { authGuard, managerGuard, helperGuard } from './route-guards';

const routes: Routes = [
  { path: 'cupboard', component: Cupboard, canActivate: [authGuard] },
  { path: 'cupboard/:id', component: NeedDetail, canActivate: [authGuard] },
  { path: 'add-need', component: AddNeed, canActivate: [managerGuard] },
  { path: 'edit-need/:id', component: AddNeed, canActivate: [managerGuard] },
  { path: 'login', component: Login },
  { path: 'basket', component: FundingbasketComponent, canActivate: [helperGuard] },
  { path: 'checkout/:id', component: Checkout, canActivate: [helperGuard] },
  { path: '', redirectTo: '/cupboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
