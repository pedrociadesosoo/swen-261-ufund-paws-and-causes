import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';
import { NeedDetail } from './need-detail/need-detail';
import { FundingbasketComponent } from './fundingbasket.component/fundingbasket.component';
import { Login } from './login/login';
import { Checkout } from './checkout/checkout';
import { Proposals } from './proposals/proposals';
import { AuthInterceptor } from './auth-interceptor';
import { NeedTypeDropdown } from './need-type-dropdown/need-type-dropdown';

@NgModule({
  declarations: [
    App,
    Cupboard,
    AddNeed,
    NeedDetail,
    FundingbasketComponent,
    Login,
    Checkout,
    NeedTypeDropdown,
    Proposals
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [App]
})
export class AppModule { }
