import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';
import { NeedDetail } from './need-detail/need-detail';
import { Login } from './login/login';

const routes: Routes = [
  { path: 'cupboard', component: Cupboard },
  { path: 'cupboard/:id', component: NeedDetail },
  { path: 'add-need', component: AddNeed },
  { path: 'login', component: Login },
  { path: '', redirectTo: '/cupboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
