import { Component, OnInit } from '@angular/core';
import { NeedService } from '../need';
import { Need } from '../need.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cupboard',
  standalone: false,
  templateUrl: './cupboard.html',
  styleUrl: './cupboard.css',
})
export class Cupboard implements OnInit {
  needs: Need[] = [];
  errorMessage: string = '';

  constructor(private needService: NeedService, private router: Router) {}

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
}