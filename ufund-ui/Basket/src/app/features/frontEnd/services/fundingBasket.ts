import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FundingBasket } from '../../frontEnd/models/funding.model';

@Injectable({ providedIn: 'root' })


export class FundingbasketService {
    private readonly API = 'http://localhost:8080/fundingbasket';


    constructor(private http: HttpClient) { }

    getFundingBasket(id: number): Observable<FundingBasket> {
        return this.http.get<FundingBasket>(`${this.API}/${id}`);
    }

    getFundingBasketArray(): Observable<FundingBasket[]> {
        return this.http.get<FundingBasket[]>(`${this.API}`);
    }

    createFundingBasket(fb: Omit<FundingBasket, 'id'>): Observable<FundingBasket> {
        return this.http.post<FundingBasket>(this.API, fb);
    }

    deleteFundingBasket(id: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.API}/${id}`);
    }

    addNeed(idFB: number, idNeed: number): Observable<boolean> {
        return this.http.post<boolean>(`${this.API}/${idFB}/${idNeed}`, null);
    }

    removeNeed(idFB: number, idNeed: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.API}/${idFB}/${idNeed}`);
    }

}