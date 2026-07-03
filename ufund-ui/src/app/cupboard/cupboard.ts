import { Component, OnInit } from '@angular/core';
import { NeedService } from '../need';
import { Need } from '../need.model';

@Component({
  selector: 'app-cupboard',
  standalone: false,
  templateUrl: './cupboard.html',
  styleUrl: './cupboard.css',
})
export class Cupboard implements OnInit {
  needs: Need[] = [];
  errorMessage: string = '';

  constructor(private needService: NeedService) {}

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
}