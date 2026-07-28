import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Organization } from '../organization';
import { OrganizationService } from '../organization.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-create-org.component',
  standalone: false,
  templateUrl: './create-org.component.html',
  styleUrl: './create-org.component.css',
})
export class CreateOrgComponent implements OnInit{
  org: Organization = {
    name: '',
    description: '',
    needs: {}
    };
  errorMessage: string = '';
  successMessage: string = '';
  // True once a save succeeds, so canDeactivate() lets the user leave without
  // a warning even though the form is still technically "dirty".
  private submitted: boolean = false;

  // Reference to the template's #needForm, used by canDeactivate() to check
  // whether the user has typed anything since the page loaded.
  @ViewChild('orgForm') orgForm?: NgForm;
  /** True when editing an existing need (route has an :id param) rather than creating a new one */
  isEditMode: boolean = false;

  constructor(
    private orgService: OrganizationService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
  const idParam = this.route.snapshot.paramMap.get('name');
  if (idParam !== null) {
    this.isEditMode = true;
    const name = String(idParam);
    this.orgService.getOrganization(name).subscribe({
      next: (org) => this.org = org,
      error: () => this.errorMessage = 'Failed to load need details'
      });
    }
  }

  /**
   * Validates the need fields before submitting
   * @returns true if valid, false otherwise
   */
  validate(): boolean {
    if (!this.org.name || this.org.name.trim() === '') {
      this.errorMessage = 'Name is required.';
      return false;
    }
    if (!this.org.description || this.org.description.trim() === '') {
      this.errorMessage = 'Description is Required';
      return false;
    }

    return true;
  }

  onSubmit(): void {
    if (!confirm(`Create organization?`)) return;
    this.errorMessage = '';
    if (!this.validate()) return;
  

    if (this.isEditMode) {
      this.orgService.updateOrganization(this.org, this.org.name).subscribe({
        next: (updated) => {
          this.successMessage = `Organization "${updated.name}" updated successfully!`;
	  this.submitted = true; // saved successfully, so the deactivate guard won't warn on this navigate
          this.router.navigate(['/organization']);
        },
        error: () => this.errorMessage = 'Failed to update organization. Please try again.'
      });
      return;
    }

    this.orgService.createOrganization(this.org).subscribe({
      next: (created) => {
        this.successMessage = `Organization "${created.name}" added successfully!`;
	      this.submitted = true; // saved successfully, so the deactivate guard won't warn on this navigate
        this.router.navigate(['/organization']);
      },
      error: (err) => {
        if (err.status === 409)
          this.errorMessage = 'An organization with that name already exists.';
        else
          this.errorMessage = 'Failed to add organization. Please try again.';
      }
    });
  }

  /**
   * Navigates back to the cupboard
   */
  goToOrgList(): void {
  if (!confirm(`Cancel organization creation?`)) return
    this.router.navigate(['/organization']);
  }
  /**
   * Called by unsavedChangesGuard before leaving this route. Safe to leave
   * silently if nothing was changed, or if the change was already saved;
   * otherwise the guard will prompt the user to confirm discarding it.
   */
  canDeactivate():boolean {
	  return this.submitted || !this.orgForm?.dirty;
  }

}
