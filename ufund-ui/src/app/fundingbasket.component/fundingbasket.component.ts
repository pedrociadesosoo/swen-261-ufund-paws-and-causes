import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need.model';
import { FundingbasketService } from '../fundingbasket.service';
import { FundingBasket } from '../fundingbasket';

/**
 * Displays all funding baskets and the needs inside them, and lets the
 * user remove needs from a basket.
 */
@Component({
  selector: 'app-fundingbasket.component',
  standalone: false,
  templateUrl: './fundingbasket.component.html',
  styleUrl: './fundingbasket.component.css',
})
export class FundingbasketComponent {
  // exposed so the template can call Object.entries() on a basket's needs
  Object = Object;
  needs: Map<number, Need> = new Map();
  errorMessage: string = '';
  fundingbasket: FundingBasket | null = null;
  id: number = 0;
  baskets: FundingBasket[] = [];

  constructor(
    private fbService: FundingbasketService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.getFundingBasketArray();
  }

  /**
   * Loads every basket from the API and re-renders
   */
  getFundingBasketArray(): void {
    this.fbService.getFundingBasketArray().subscribe({
      next: (baskets) => {
        this.baskets = baskets;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Loading failure';
        this.cdr.detectChanges();
      }
    })
  }

  /**
   * Refreshes a single basket in place without reloading the whole list
   */
  getFundingBasket(id: number): void {
      this.fbService.getFundingBasketArray().subscribe({
        next: (baskets) => {
          const updated = baskets.find(b => b.id === id);
          if (updated) {
            const index = this.baskets.findIndex(b => b.id === id);
            if (index !== -1) {
              this.baskets[index] = updated;
            }
          }
      },
      error: () => {
        this.errorMessage = 'Loading failure';
      }
    })
  }

  /**
   * Adds a need to a basket, then reloads the list on success
   */
  add(idFB: number, idNeed: number): void {
    this.fbService.addNeed(idFB, idNeed).subscribe({
      next: (success) => {
        if (success) {
          this.getFundingBasketArray();
        } else {
          this.errorMessage = 'Unable to add need to basket';
        }
      },
      error: () => {
        this.errorMessage = 'Failure adding need to basket';
      }
    });
  }

  /**
   * Removes a need from a basket, then refreshes that basket on success
   */
  remove(idFB: number, idNeed: number): void {
    this.fbService.removeNeed(idFB, idNeed).subscribe({
      next: (success) => {
        if(success) {
          this.getFundingBasket(idFB);
        } else  {
          this.errorMessage = 'Unable to delete need from basket'
        }
      },
        error: () => {
          this.errorMessage = 'Failure to delete need from basket'
        }
    })

  }

}
