import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';
import { NeedDetail } from './need-detail/need-detail';
import { Login } from './login/login';
import { FundingbasketComponent } from './fundingbasket.component/fundingbasket.component';
import { Checkout } from './checkout/checkout';
import { OrganizationListComponent } from './organization-list.component/organization-list.component';
import { CreateOrgComponent } from './create-org.component/create-org.component';
import { OrganizationDetailsComponent } from './organization-details.component/organization-details.component';
import { Proposals } from './proposals/proposals';
import { CreateProposal } from './create-proposal/create-proposal';
import { authGuard, managerGuard, helperGuard, unsavedChangesGuard, unsavedProposalGuard } from './route-guards';

const routes: Routes = [
  { path: 'organization', component: OrganizationListComponent, canActivate: [authGuard]},
  {path: 'organization/:name', component: OrganizationDetailsComponent, canActivate: [authGuard]},
  { path: 'organization_edit', component: CreateOrgComponent, canActivate: [authGuard], canDeactivate: [unsavedChangesGuard]},
  { path: 'edit-org/:name', component: CreateOrgComponent, canActivate: [authGuard], canDeactivate: [unsavedChangesGuard]},
  { path: 'cupboard', component: Cupboard, canActivate: [authGuard] },
  { path: 'cupboard/:id', component: NeedDetail, canActivate: [authGuard] },
  { path: 'add-need', component: AddNeed, canActivate: [managerGuard], canDeactivate: [unsavedChangesGuard] },
  { path: 'edit-need/:id', component: AddNeed, canActivate: [managerGuard], canDeactivate: [unsavedChangesGuard] },
  { path: 'login', component: Login },
  { path: 'basket', component: FundingbasketComponent, canActivate: [helperGuard] },
  { path: 'checkout/:id', component: Checkout, canActivate: [helperGuard] },
  { path: 'proposals', component: Proposals, canActivate: [authGuard], canDeactivate: [unsavedProposalGuard] },
  { path: 'create-proposal', component: CreateProposal, canActivate: [helperGuard], canDeactivate: [unsavedChangesGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
