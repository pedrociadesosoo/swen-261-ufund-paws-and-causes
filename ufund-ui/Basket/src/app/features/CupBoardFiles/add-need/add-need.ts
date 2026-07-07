import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NeedService } from '../../frontEnd/services/need.service';
import { Need } from '../../frontEnd/models/need.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-add-need',
  imports: [CommonModule, FormsModule],
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
   * Submits the form to create a new need
   */
  onSubmit(): void {
    this.needService.create(this.need).subscribe({
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
