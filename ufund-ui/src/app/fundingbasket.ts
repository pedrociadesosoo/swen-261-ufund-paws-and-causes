import { Need } from "./need.model";

export interface FundingBasket {
    id: number;
    needs: Map<number, Need>;
}