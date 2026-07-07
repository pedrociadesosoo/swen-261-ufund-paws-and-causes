import { Component, OnInit } from '@angular/core';
import { NeedService } from '../need';
import { Need } from '../need.model';
import { Router } from '@angular/router';
import { AccountService } from '../account';
import { FundingbasketService } from '../fundingbasket.service';

@Component({
  selector: 'app-cupboard',
  standalone: false,
  templateUrl: './cupboard.html',
  styleUrl: './cupboard.css',
})
export class Cupboard implements OnInit {
  needs: Need[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private needService: NeedService,
    private router: Router,
    private accountService: AccountService,
    private fbService: FundingbasketService
  ) {}

  ngOnInit(): void {
    this.loadNeeds();
  }

  /**
   * Loads all needs from the API
   */
  loadNeeds(): void {
    this.needService.getNeeds().subscribe({
      next: (needs) => this.needs = needs,
      error: () => this.errorMessage = 'Failed to load needs'
    });
  }

  /**
   * Navigates to the need detail page
   */
  viewNeed(id: number): void {
    this.router.navigate(['/cupboard', id]);
  }

  /**
   * Adds a need to the funding basket, creating the basket first if
   * none exists yet
   */
  addToBasket(need: Need): void {
    this.fbService.getFundingBasketArray().subscribe({
      next: (baskets) => {
        if (baskets.length === 0) {
          this.fbService.createFundingBasket({ id: 0, needs: {} }).subscribe({
            next: (basket) => this.addNeedToBasket(basket.id, need),
            error: () => this.errorMessage = 'Failed to create a basket'
          });
        } else {
          this.addNeedToBasket(baskets[0].id, need);
        }
      },
      error: () => this.errorMessage = 'Failed to load baskets'
    });
  }

  private addNeedToBasket(basketId: number, need: Need): void {
    this.fbService.addNeed(basketId, need.id).subscribe({
      next: () => {
        this.errorMessage = '';
        this.successMessage = `${need.name} added to basket`;
      },
      error: (err) => {
        this.successMessage = '';
        this.errorMessage = err.status === 409
          ? `${need.name} is already in the basket`
          : 'Failed to add to basket';
      }
    });
  }

  /**
   * Returns true if the current user is a manager
   */
  isManager(): boolean {
    return this.accountService.isManager();
  }

  /**
   * Returns true if the current user is a helper
   */
  isHelper(): boolean {
    return this.accountService.isHelper();
  }
}