import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { FundingBasket } from './fundingbasket';
import { Need } from './need.model';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class FundingbasketService {

  constructor(
    private http: HttpClient,) { }
  /** Log a HeroService message with the MessageService */

  getFundingBasket(id: number): Observable<FundingBasket> {
    const url = `${this.fbUrl}/${id}`;
    return this.http.get<FundingBasket>(url).pipe(
      tap(() => console.log(`fetched funding basket id=${id}`)),
      catchError(this.handleError<FundingBasket>(`Get Funding Basket id=${id}`))
    );
  }

  addNeed(idFB: number, idNeed: number): Observable<Boolean>{
    const url = `${this.fbUrl}/${idFB}/${idNeed}`;
    return this.http.post<boolean>(url, {}).pipe(
      tap(() => console.log(`added need ${idNeed} to funding basket ${idFB}`)),
      catchError(this.handleError<boolean>(`add need`))
    );
  }

  removeNeed(idFB: number, idNeed: number): Observable<Boolean>{
    const url = `${this.fbUrl}/${idFB}/${idNeed}`;
    return this.http.delete<boolean>(url, {}).pipe(
      tap(() => console.log(`deleted need ${idNeed} from funding basket ${idFB}`)),
      catchError(this.handleError<boolean>(`delete need`))
    );
  }

  /**
  * Handle Http operation that failed.
  * Let the app continue.
  *
  * @param operation - name of the operation that failed
  * @param result - optional value to return as the observable result
  */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: unknown): Observable<T> => {
      console.error(`${operation} failed`, error);
      return of(result as T);
    };
  }

  private fbUrl = 'http://localhost:8080/fundingbasket';
}
