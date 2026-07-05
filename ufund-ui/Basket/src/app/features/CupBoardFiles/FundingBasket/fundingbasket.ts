import { Component } from '@angular/core';
import { NeedService } from '../../frontEnd/services/need.service';
import { Need } from '../../frontEnd/models/need.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-fundingbasket.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './fundingbasket.html',
  styleUrl: './fundingbasket.css',
})
export class FundingbasketComponent {
  needs: Need[] = [];

 // constructor(private fbService: FundingbasketService) { }

  ngOnInit(): void{
    this.getFundingBasket();
  }
  
  getFundingBasket(): void {
  //  this.
  }

  add(need: Need): void {

  }

  remove(need: Need): void {

  }

}
