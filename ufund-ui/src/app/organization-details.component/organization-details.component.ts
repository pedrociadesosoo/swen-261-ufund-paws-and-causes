import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Organization } from '../organization';
import { OrganizationService } from '../organization.service';

@Component({
  selector: 'app-organization-details.component',
  standalone: false,
  templateUrl: './organization-details.component.html',
  styleUrl: './organization-details.component.css',
})
export class OrganizationDetailsComponent {
  Object = Object;
  org: Organization | null = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private orgService: OrganizationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const name = String(this.route.snapshot.paramMap.get('name'));
    this.orgService.getOrganization(name).subscribe({
      next: (org) => this.org = org,
      error: () => this.errorMessage = 'Failed to load need details'
    });
  }

  /**
   * Navigates back to the cupboard
   */
  goBack(): void {
    this.router.navigate(['/organization']);
  }
}
