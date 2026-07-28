import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Organization } from '../organization';
import { Need } from '../need.model';
import { OrganizationService } from '../organization.service';
import { AccountService } from '../account';
import { OrganizationDetailsComponent } from '../organization-details.component/organization-details.component';

@Component({
  selector: 'app-organization-list.component',
  standalone: false,
  templateUrl: './organization-list.component.html',
  styleUrl: './organization-list.component.css',
})

export class OrganizationListComponent {
    Object = Object;
    needs: Map<number, Need> = new Map();
    errorMessage: string = '';
    organization: Organization | null = null;
    id: number = 0;
    orgs: Organization[] = [];

    constructor(
    private orgService: OrganizationService,
    private accountService: AccountService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(){
    this.getOrganizationArray();
  }

  createOrganization(org: Organization): void{
    this.orgService.createOrganization(org).subscribe({
      next: (success) => {
        if (success) {
          this.getOrganizationArray();
        } else {
          this.errorMessage = 'Failed to create organization';
        }
      }, 
        error: () => {
        this.errorMessage = 'Failed to create organization';
        this.cdr.detectChanges();
      }
    })
  }

  deleteOrganization(name: string): void{
    if (!confirm(`Delete organization ${name}?`)) return
    this.orgService.deleteOrganization(name).subscribe({
      next: (success) => {
        if (success) {
          this.getOrganizationArray();
        } else {
          this.errorMessage = 'Failed to create organization';
        }
      }, 
        error: () => {
        this.errorMessage = 'Failed to create organization';
        this.cdr.detectChanges();
      }
    })
  }

  /**
   * Manager-only: navigates to the edit form for an organization
   */
  editOrganization(name: string): void {
    this.router.navigate(['/edit-org', name]);
  }

  getOrganizationArray(): void {
    this.orgService.getOrganizationArray().subscribe({
      next: (orgs) => {
        this.orgs = orgs;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Loading failure';
        this.cdr.detectChanges();
      }
    })
  }

  getOrganization(name: string): void {
    this.orgService.getOrganizationArray().subscribe({
      next: (orgs) => {
          const updated = orgs.find(o => o.name === name);
          if (updated) {
            const index = this.orgs.findIndex(o => o.name === name);
            if (index !== -1) {
              this.orgs[index] = updated;
            }
          }
      },
      error: () => {
        this.errorMessage = 'Loading failure';
        this.cdr.detectChanges();
      }
    })
  }

  updateOrganization(name: string): void {
    this.orgService.getOrganizationArray().subscribe({
      next: (orgs) => {
          const updated = orgs.find(o => o.name === name);
          if (updated) {
            const index = this.orgs.findIndex(o => o.name === name);
            if (index !== -1) {
              this.orgs[index] = updated;
            }
          }
      },
      
      error: () => {
        this.errorMessage = 'Loading failure';
        this.cdr.detectChanges();
      }
    })
  }

  addNeed(name: string, id: number): void {
    this.orgService.addNeed(name,id).subscribe({
      next: (success) => {
        if (success){
          this.getOrganizationArray();
        } else {
          this.errorMessage = 'Unable to add need to organization';
        }
      },
      error: () => {
        this.errorMessage = 'Failure adding need to organization';
      }
     })
  }

  deleteNeed(name: string, id: number): void {
    this.orgService.deleteNeed(name,id).subscribe({
      next: (success) => {
        if (success) {
          this.getOrganizationArray();
        } else {
          this.errorMessage = 'Unable to delete need from organization';
        }
      },
      error: () => {
        this.errorMessage = 'Failure to delete need from organization'
      }
    })
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
