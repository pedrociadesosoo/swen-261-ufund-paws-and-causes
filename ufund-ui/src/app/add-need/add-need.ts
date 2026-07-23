import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NeedService } from '../need';
import { Need } from '../need.model';
import { NgForm } from '@angular/forms';
import { NeedType } from '../need-type';

@Component({
  selector: 'app-add-need',
  standalone: false,
  templateUrl: './add-need.html',
  styleUrl: './add-need.css',
})
export class AddNeed implements OnInit {
  /** Exposed so the template's dropdown can bind to NeedType.ITEM_DONATION etc. */
  NeedType = NeedType;

  need: Need = {
    id: 0,
    name: '',
    cost: 0,
    quantity: 0,
    type: NeedType.SELECT
  };
  errorMessage: string = '';
  successMessage: string = '';

  private submitted: boolean = false;

 
  @ViewChild('needForm') needForm?: NgForm;
  /** True when editing an existing need rather than creating a new one */
  isEditMode: boolean = false;

  constructor(
    private needService: NeedService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  /**
   * If the route was loaded with an :id param, this is the edit page for an
   * existing need, so load its current values into the form.
   */
  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam !== null) {
      this.isEditMode = true;
      const id = Number(idParam);
      this.needService.getNeedById(id).subscribe({
        next: (need) => this.need = need,
        error: () => this.errorMessage = 'Failed to load need details'
      });
    }
  }

  /**
   * Validates the need fields before submitting
   * @returns true if valid, false otherwise
   */
  validate(): boolean {
    if (!this.need.name || this.need.name.trim() === '') {
      this.errorMessage = 'Name is required.';
      return false;
    }
    if (this.need.cost < 0) {
      this.errorMessage = 'Cost must be greater than 0.';
      return false;
    }
    if (this.need.quantity <= 0) {
      this.errorMessage = 'Quantity must be greater than 0.';
      return false;
    }
    if (this.need.type == NeedType.SELECT) {
      this.errorMessage = 'Type is required.';
      return false;
    }
    return true;
  }

  /**
   * Submits the form, either creating a new need or updating the existing
   * one being edited, depending on isEditMode.
   */
  onSubmit(): void {
    this.errorMessage = '';
    if (!this.validate()) return;

    if (this.isEditMode) {
      this.needService.updateNeed(this.need.id, this.need).subscribe({
        next: (updated) => {
          this.successMessage = `Need "${updated.name}" updated successfully!`;
	  this.submitted = true; 
          this.router.navigate(['/cupboard']);
        },
        error: () => this.errorMessage = 'Failed to update need. Please try again.'
      });
      return;
    }

    this.needService.createNeed(this.need).subscribe({
      next: (created) => {
        this.successMessage = `Need "${created.name}" added successfully!`;
	this.submitted = true; 
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
  /**
   * Called by unsavedChangesGuard before leaving this route. Safe to leave
   * silently if nothing was changed, or if the change was already saved;
   * otherwise the guard will prompt the user to confirm discarding it.
   */
  canDeactivate():boolean {
	  return this.submitted || !this.needForm?.dirty;
  }
}
