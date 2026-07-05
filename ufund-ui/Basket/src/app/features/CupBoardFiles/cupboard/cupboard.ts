import { Component, OnInit } from '@angular/core';
import { NeedService } from '../../frontEnd/services/need.service';
import { Need } from '../../frontEnd/models/need.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cupboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './cupboard.html',
  styleUrl: './cupboard.css',
})
export class Cupboard implements OnInit {
  needs: Need[] = [];
  selectedNeed: any;

  errorMessage: string = '';

  constructor(private needService: NeedService, private router: Router) {}

  ngOnInit(): void {
    this.loadNeeds();
  }

  /**
   * Loads all needs from the API
   */
  loadNeeds(): void {
    this.needService.getAll().subscribe({
      next: (needs: Need[]) => this.needs = needs,
      error: () => this.errorMessage = 'Failed to load needs'
    });
  }

  /**
   * Navigates to the need detail page
   */
  viewNeed(id: number): void {
    this.router.navigate(['/cupboard', id]);
  }

  editNeed(): void {
    this.router.navigate(['/edit-need']);
  }

  addNeed(): void {
    this.router.navigate(['/add-need']);
  }
}