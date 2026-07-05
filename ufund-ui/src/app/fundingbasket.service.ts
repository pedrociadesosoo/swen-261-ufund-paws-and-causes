import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { FundingBasket } from './fundingbasket';
import { Need } from './need.model';

@Injectable({
  providedIn: 'root',
})

export class FundingbasketService {

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }
  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`HeroService: ${message}`);
  }

  getFundingBasket(id: number): Observable<FundingBasket> {
    const url = `${this.fbUrl}/${id}`;
    return this.http.get<FundingBasket>(url).pipe(
      tap(_ => this.log(`fetched fb id=${id}`)),
      catchError(this.handleError<FundingBasket>(`getFundingBaskset id=${id}`))
    );
  }

  addNeed(need: Need): Observable<Need>{
    return this.http.post<Need>(this.fbUrl, need, this.httpOptions).pipe(
    tap((newNeed: Need) => this.log(`added need w/ id=${newNeed.id}`)),
    catchError(this.handleError<Need>('addneed')));
  }

  /**
  * Handle Http operation that failed.
  * Let the app continue.
  *
  * @param operation - name of the operation that failed
  * @param result - optional value to return as the observable result
  */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private fbUrl = 'http://localhost:8080/fundingbasket';
}
