import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NeedService } from '../../frontEnd/services/need.service';
import { Need } from '../../frontEnd/models/need.model';
import { ActivatedRoute, Router } from '@angular/router';



const Needs_url = "/needs";

@Component({
  selector: 'app-edit-need',
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-need.html',
  styleUrl: './edit-need.css',
})


export class EditNeed {
    data: any;
    selectedNeed: any;
    errorMessage: string = '';

    
    constructor(
      private route: ActivatedRoute,
      private needService: NeedService,
      private router: Router
    ) {}

    need: Need = {
      id: 0,
      name: '',
      cost: 0,
      quantity: 0,
      type: ''
    };


    ngOnInit() {
      this.loadNeeds();
    }

    loadNeeds(): void {
    this.needService.getAll().subscribe({
      next: (needs: any[]) => this.data = needs,
      error: () => this.errorMessage = 'Failed to load needs'
    });
  }

    edit(item: any) { 
      this.selectedNeed = { ...item };
    }

    remove(id: number) { 
      this.needService.delete(id).subscribe(() => {
        this.data = this.data.filter((n: any) => n.id !== id);
          /** Array.filter
            https://www.geeksforgeeks.org/typescript/typescript-array-filter-method/
          */
        });
    }

    save() {
      this.needService.update(this.selectedNeed.id, this.selectedNeed).subscribe(() => {
        const index = this.data.findIndex((n: any) => n.id === this.selectedNeed.id);
        this.data[index] = { ...this.selectedNeed };
        this.selectedNeed = null;
      });
    }


  Cupboard(): void {
    this.router.navigateByUrl('/cupboard');
  }
}