import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Cupboard } from './cupboard/cupboard';
import { AddNeed } from './add-need/add-need';
import { NeedDetail } from './need-detail/need-detail';
import { FundingbasketComponent } from './fundingbasket.component/fundingbasket.component';
import { Login } from './login/login';

@NgModule({
  declarations: [
    App,
    Cupboard,
    AddNeed,
    NeedDetail,
    FundingbasketComponent,
    Login
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners()
  ],
  bootstrap: [App]
})
export class AppModule { }
