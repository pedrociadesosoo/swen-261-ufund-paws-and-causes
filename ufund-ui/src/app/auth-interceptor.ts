import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountService } from './account';

/**
 * Attaches the current account's username to every outgoing API request via
 * an X-Username header. This lets the backend enforce manager-only actions
 * (creating/editing/deleting needs) itself, instead of relying solely on the
 * UI hiding buttons and routes for non-managers.
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private accountService: AccountService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const username = this.accountService.getCurrentAccount()?.username;
    if (!username) {
      return next.handle(req);
    }
    const authorizedReq = req.clone({ setHeaders: { 'X-Username': username } });
    return next.handle(authorizedReq);
  }
}
