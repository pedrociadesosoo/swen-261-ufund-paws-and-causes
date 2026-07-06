import { Component } from '@angular/core';
import { Need } from '../../frontEnd/models/need.model';
import { FundingbasketService } from '../../frontEnd/services/fundingBasket';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { FundingBasket } from '../../frontEnd/models/funding.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-fundingbasket.component',
  templateUrl: './fundingbasket.html',
  styleUrl: './fundingbasket.css',
  imports: [RouterLink, RouterOutlet, CommonModule, FormsModule],
})
export class FundingbasketComponent {
  Object = Object;
  needs: Map<number, Need> = new Map();
  errorMessage: string = '';
  fundingbasket: FundingBasket | null = null;
  id: number = 0;
  baskets: FundingBasket[] = [];

  constructor(private fbService: FundingbasketService, private router: Router) { }

  ngOnInit(): void {
    this.getFundingBasketArray();
  }

  getFundingBasketArray(): void {
    this.fbService.getFundingBasketArray().subscribe({
      next: (baskets) => {
        this.baskets = baskets;
      },
      error: () => {
        this.errorMessage = 'Loading failure';
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