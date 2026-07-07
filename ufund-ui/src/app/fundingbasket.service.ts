import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FundingBasket } from './fundingbasket';

/**
 * Makes the funding-basket API calls to the backend.
 * Mirrors the endpoints on FundingBasketController.
 */
@Injectable({ providedIn: 'root' })
export class FundingbasketService {
    private readonly API = 'http://localhost:8080/fundingbasket';

    constructor(private http: HttpClient) { }

    /**
     * Fetches a single basket by id
     */
    getFundingBasket(id: number): Observable<FundingBasket> {
        return this.http.get<FundingBasket>(`${this.API}/${id}`);
    }

    /**
     * Fetches every basket
     */
    getFundingBasketArray(): Observable<FundingBasket[]> {
        return this.http.get<FundingBasket[]>(`${this.API}`);
    }

    /**
     * Creates a basket; the backend assigns the real id
     */
    createFundingBasket(fb: FundingBasket): Observable<FundingBasket> {
        return this.http.post<FundingBasket>(this.API, fb);
    }

    /**
     * Deletes a basket by id
     */
    deleteFundingBasket(id: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.API}/${id}`);
    }

    /**
     * Adds an existing cupboard need to a basket, both referenced by id.
     * 409 from the backend means the need is already in the basket.
     */
    addNeed(idFB: number, idNeed: number): Observable<boolean> {
        return this.http.post<boolean>(`${this.API}/${idFB}/${idNeed}`, null);
    }

    /**
     * Removes a need from a basket, both referenced by id
     */
    removeNeed(idFB: number, idNeed: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.API}/${idFB}/${idNeed}`);
    }

}
