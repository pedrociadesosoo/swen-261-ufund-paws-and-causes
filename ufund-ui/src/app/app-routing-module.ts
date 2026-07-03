import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';

const routes: Routes = [
  { path: 'cupboard', component: Cupboard},
  { path: 'add-need', component: AddNeed},
  { path: '', redirectTo: '/cupboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
