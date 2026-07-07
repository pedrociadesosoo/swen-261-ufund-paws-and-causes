import { Component, OnInit } from '@angular/core';
import { NeedService } from '../../frontEnd/services/need.service';
import { Need } from '../../frontEnd/models/need.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccountStateService } from '../../frontEnd/services/account-state.service';
import { ChangeDetectorRef } from '@angular/core';
import { FundingbasketService } from '../../frontEnd/services/fundingBasket';

@Component({
  selector: 'app-cupboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './cupboard.html',
  styleUrl: './cupboard.css',
})
export class Cupboard implements OnInit {
  needs: Need[] = [];
  selectedNeed: any;
  successMessage: string = '';
  errorMessage: string = '';
  data: any;
  baskets: any[] = [];
  selectedBasketId: number = 1;

  constructor(
    private needService: NeedService, 
    private router: Router,
    private authService: AccountStateService,
    private cdr: ChangeDetectorRef,
    private fundingBasketService: FundingbasketService
  ) {}

  ngOnInit(): void {
    this.loadNeeds();
    this.loadBaskets();
  }


  getNextBasketId(): number {
    if (this.baskets.length === 0) {
      return 1;
    }
    return Math.max(...this.baskets.map(b => b.id)) + 1;
  }

  createBasket(): void {
    const newId = this.getNextBasketId();

    const newBasket = { id: newId, needs: {} };


    this.fundingBasketService.createFundingBasket(newBasket).subscribe({
      next: (basket) => {
        this.baskets.push(basket);
        this.selectedBasketId = basket.id;
      },
      error: () => {
        this.errorMessage = 'Failed to create new basket.';
      }
    });
  }


  deleteBasket(id: number): void {
    this.fundingBasketService.deleteFundingBasket(id).subscribe({
      next: () => {
        this.loadBaskets();
        this.selectedBasketId = 0;
        },
      error: () => {
        this.errorMessage = 'Failed to delete basket.';
      }
    });
  }



  loadBaskets(): void {
    this.fundingBasketService.getFundingBasketArray().subscribe({
      next: (baskets: any[]) => {
        this.baskets = baskets;
      }
    });
  }
  /**
   * Loads all needs from the API
   */
  loadNeeds(): void {
    this.needService.getAll().subscribe({
      next: (needs: Need[]) =>  {
        this.needs = needs;
        this.cdr.detectChanges();
      },
      error: () => this.errorMessage = 'Failed to load needs'
    });
  }

  /**
   * Navigates to the need detail page
   */
  viewNeed(id: number): void {
    this.router.navigate(['/need-detail', id]);
  }

  addNeed(): void {
    this.router.navigate(['/add-need']);
  }

  viewFundingBasket(): void {
    this.router.navigate(['/funding-basket']);
  }

  addFundingBasket(id: number): void {
    const basketId = id;  
    const needId = this.selectedNeed.id;

    this.fundingBasketService.addNeed(basketId, needId).subscribe({
      next: () => {
        this.successMessage = `Need "${this.selectedNeed.name}" added to funding basket successfully!`;
        this.router.navigate(['/funding-basket', basketId]);
      },
      error: (err) => {
        if (err.status === 409)
          this.errorMessage = 'This need is already in the funding basket.';
        else
          this.errorMessage = 'Failed to add need to funding basket. Please try again.';
      }
    });
  }



  edit(item: Need) { 
      this.selectedNeed = { ...item };
    }

    remove(id: number) { 
      this.needService.delete(id).subscribe(() => {
        this.needs = this.needs.filter((n: any) => n.id !== id);
        });
    }

    save() {
      this.needService.update(this.selectedNeed.id, this.selectedNeed).subscribe(() => {
        const index = this.needs.findIndex((n: any) => n.id === this.selectedNeed.id);
        this.needs[index] = { ...this.selectedNeed };
        this.selectedNeed = null;
      });
    }
    exitEdit() {
      this.selectedNeed = null;
    }

    /**
   * Returns true if the current user is a manager
   */
  isManager(): boolean {
    return this.authService.isAdmin();
  }

  /**
   * Returns true if the current user is a helper
   */
  isHelper(): boolean {
    return this.authService.role() == 'helper';
  }
}