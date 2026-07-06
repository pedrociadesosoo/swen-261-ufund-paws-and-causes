import { Routes } from '@angular/router';
import {EditNeed} from './features/CupBoardFiles/edit-need/edit-need'
import {Cupboard} from './features/CupBoardFiles/cupboard/cupboard'
import {NeedDetail} from './features/CupBoardFiles/need-detail/need-detail'
import {AddNeed} from './features/CupBoardFiles/add-need/add-need'
import { FundingbasketComponent } from './features/CupBoardFiles/FundingBasket/fundingbasket';

// components
import {AdminComponent} from './features/CupBoardFiles/components/admin/admin'
import {BasketComponent} from './features/CupBoardFiles/components/basket/basket'
import { LoginComponent } from './features/CupBoardFiles/components/login/login'
import {NeedListComponent} from './features/CupBoardFiles/components/need-list/need-list'
import {NeedDetailComponent} from './features/CupBoardFiles/components/need-detail/need-detail'


export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'edit-need', component: EditNeed },
    { path: 'cupboard', component: Cupboard },
    { path: 'need-detail', component: NeedDetail },
    { path: 'add-need', component: AddNeed },
    { path: 'admin', component: AdminComponent },
    { path: 'basket', component: BasketComponent },
    { path: 'funding-basket', component: FundingbasketComponent },
    { path: 'needs', component: NeedListComponent },
    { path: 'details_component/:id', component: NeedDetailComponent }
];

