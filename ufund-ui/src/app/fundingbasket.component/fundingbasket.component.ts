import { Component } from '@angular/core';
import { Need } from '../need.model';
import { FundingbasketService } from '../fundingbasket.service';
import { NeedService } from '../need';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { FundingBasket } from '../fundingbasket';


@Component({
  selector: 'app-fundingbasket.component',
  standalone: false,
  templateUrl: './fundingbasket.component.html',
  styleUrl: './fundingbasket.component.css',
})
export class FundingbasketComponent {
  needs: Map<number, Need> = new Map<number, Need>();
  errorMessage: string = '';
  fundingbasket: FundingBasket | null = null;
  id: number = 0;

  constructor(private fbService: FundingbasketService, private router: Router) { }

  ngOnInit(): void {
    this.createNewBasket();
  }

  createNewBasket(): void {
    this.id = this.id;
    this.id++;
    this.getFundingBasket(this.id); 
  }

  getFundingBasket(id: number): void {
    this.fbService.getFundingBasket(id).subscribe({
      next: (basket) => {
        this.fundingbasket = basket;
        this.needs = new Map<number, Need>(Object.entries(basket.needs ?? {}).map(([key,value]) => [Number (key), value as Need]));
      },
      error: () => {
        this.errorMessage = 'Loading failure';
      }
    })
  }

  add(idFB: number, idNeed: number): void {
    if (this.needs.has(idNeed)){
      this.errorMessage = 'Need already in basket';
      return;
    }

    this.fbService.addNeed(idFB, idNeed).subscribe({
      next: (success) => {
        if (success) {
        this.getFundingBasket(idFB);
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
