import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundingbasketService } from '../fundingbasket.service';
import { FundingBasket } from '../fundingbasket';

/**
 * Walks a helper through completing (or cancelling) checkout for a single
 * funding basket. On success the backend fully funds every need in the
 * basket and clears it; see FundingBasketController#checkout.
 */
@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout implements OnInit {
  basketId: number = 0;
  basket: FundingBasket | null = null;
  errorMessage: string = '';
  successMessage: string = '';

  // "Necessary fields" for completing checkout, per the sprint acceptance criteria
  nameOnCard: string = '';
  cardNumber: string = '';
  expiration: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fbService: FundingbasketService
  ) {}

  ngOnInit(): void {
    this.basketId = Number(this.route.snapshot.paramMap.get('id'));
    this.fbService.getFundingBasket(this.basketId).subscribe({
      next: (basket) => {
        this.basket = basket;
        // An empty basket can't be checked out; bounce back to the basket page.
        if (this.needCount() === 0) {
          this.errorMessage = 'Your basket is empty, so there is nothing to check out.';
        }
      },
      error: () => this.errorMessage = 'Failed to load basket'
    });
  }

  /**
   * Number of needs currently in the basket
   */
  needCount(): number {
    return this.basket ? Object.keys(this.basket.needs ?? {}).length : 0;
  }

  /**
   * True if every required checkout field has been filled in
   */
  private fieldsComplete(): boolean {
    return this.nameOnCard.trim() !== ''
      && this.cardNumber.trim() !== ''
      && this.expiration.trim() !== '';
  }

  /**
   * Submits checkout once the required fields are filled and the basket
   * isn't empty; tells the helper what's missing otherwise.
   */
  completeCheckout(): void {
    this.errorMessage = '';
    if (this.needCount() === 0) {
      this.errorMessage = 'Your basket is empty, so there is nothing to check out.';
      return;
    }
    if (!this.fieldsComplete()) {
      this.errorMessage = 'Please complete all required fields before checking out.';
      return;
    }

    this.fbService.checkout(this.basketId).subscribe({
      next: () => {
        this.successMessage = 'Thank you! Your contribution was submitted successfully.';
        this.router.navigate(['/basket']);
      },
      error: () => this.errorMessage = 'Checkout failed. Please try again.'
    });
  }

  /**
   * Asks the helper to confirm before abandoning checkout. Confirming
   * returns to the basket unchanged; declining lets them continue checkout.
   */
  cancelCheckout(): void {
    const reallyCancel = confirm('Cancel checkout and return to your basket? Nothing will be charged.');
    if (reallyCancel) {
      this.router.navigate(['/basket']);
    }
    // If they cancel the cancellation, we simply stay on the checkout page.
  }
}
