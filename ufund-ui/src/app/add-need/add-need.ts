import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NeedService } from '../need';
import { Need } from '../need.model';

@Component({
  selector: 'app-add-need',
  standalone: false,
  templateUrl: './add-need.html',
  styleUrl: './add-need.css',
})
export class AddNeed {
  need: Need = {
    id: 0,
    name: '',
    cost: 0,
    quantity: 0,
    type: ''
  };
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private needService: NeedService, private router: Router) {}

  /**
   * Validates the need fields before submitting
   * @returns true if valid, false otherwise
   */
  validate(): boolean {
    if (!this.need.name || this.need.name.trim() === '') {
      this.errorMessage = 'Name is required.';
      return false;
    }
    if (this.need.cost <= 0) {
      this.errorMessage = 'Cost must be greater than 0.';
      return false;
    }
    if (this.need.quantity <= 0) {
      this.errorMessage = 'Quantity must be greater than 0.';
      return false;
    }
    if (!this.need.type || this.need.type.trim() === '') {
      this.errorMessage = 'Type is required.';
      return false;
    }
    return true;
  }

  /**
   * Submits the form to create a new need
   */
  onSubmit(): void {
    this.errorMessage = '';
    if (!this.validate()) return;

    this.needService.createNeed(this.need).subscribe({
      next: (created) => {
        this.successMessage = `Need "${created.name}" added successfully!`;
        this.router.navigate(['/cupboard']);
      },
      error: (err) => {
        if (err.status === 409)
          this.errorMessage = 'A need with that name already exists.';
        else
          this.errorMessage = 'Failed to add need. Please try again.';
      }
    });
  }

  /**
   * Navigates back to the cupboard
   */
  goToCupboard(): void {
    this.router.navigate(['/cupboard']);
  }
}