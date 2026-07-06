import { Need } from "./need.model";

export interface FundingBasket {
  id: number;
  needs: { [key: number]: Need };
}
