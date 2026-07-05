import { Component } from '@angular/core';
import { Need } from '../need.model';
import { FundingbasketService } from '../fundingbasket.service';


@Component({
  selector: 'app-fundingbasket.component',
  standalone: false,
  templateUrl: './fundingbasket.component.html',
  styleUrl: './fundingbasket.component.css',
})
export class FundingbasketComponent {
  needs: Need[] = [];

  constructor(private fbService: FundingbasketService) { }

  ngOnInit(): void{
    this.getFundingBasket();
  }
  
  getFundingBasket(): void {
    this.
  }

  add(need: Need): void {

  }

  remove(need: Need): void {

  }

}
