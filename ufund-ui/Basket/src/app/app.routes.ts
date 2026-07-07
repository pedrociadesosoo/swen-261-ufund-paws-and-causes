import { Routes } from '@angular/router';


import {Cupboard} from './features/CupBoardFiles/cupboard/cupboard'
import {NeedDetail} from './features/CupBoardFiles/need-detail/need-detail'
import {AddNeed} from './features/CupBoardFiles/add-need/add-need'
import { FundingbasketComponent } from './features/CupBoardFiles/FundingBasket/fundingbasket';

// components
import {AdminComponent} from './features/CupBoardFiles/components/admin/admin'
import {BasketComponent} from './features/CupBoardFiles/components/basket/basket'
import { LoginComponent } from './features/CupBoardFiles/components/login/login'
import {NeedListComponent} from './features/CupBoardFiles/components/need-list/need-list'


export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'cupboard', component: Cupboard },
    { path: 'need-detail/:id', component: NeedDetail },
    { path: 'add-need', component: AddNeed },
    { path: 'admin', component: AdminComponent },
    { path: 'basket', component: BasketComponent },
    { path: 'funding-basket', component: FundingbasketComponent },
    { path: 'funding-basket/:id', component: FundingbasketComponent },
    { path: 'needs', component: NeedListComponent },
];

