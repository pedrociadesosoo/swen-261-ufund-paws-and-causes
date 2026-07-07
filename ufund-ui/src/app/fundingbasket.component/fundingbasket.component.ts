import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need.model';
import { FundingbasketService } from '../fundingbasket.service';
import { FundingBasket } from '../fundingbasket';

@Component({
  selector: 'app-fundingbasket.component',
  standalone: false,
  templateUrl: './fundingbasket.component.html',
  styleUrl: './fundingbasket.component.css',
})
export class FundingbasketComponent {
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
