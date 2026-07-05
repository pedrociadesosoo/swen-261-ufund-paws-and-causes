import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NeedService } from '../../frontEnd/services/need.service';
import { Need } from '../../frontEnd/models/need.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-need-detail',
  imports: [CommonModule, FormsModule],
  templateUrl: './need-detail.html',
  styleUrl: './need-detail.css',
})


export class NeedDetail implements OnInit {
  need: Need | null = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.needService.getById(id).subscribe({
      next: (need: Need) => this.need = need,
      error: () => this.errorMessage = 'Failed to load need details'
    });
  }

  /**
   * Navigates back to the cupboard
   */
  goBack(): void {
    this.router.navigate(['/cupboard']);
  }
}