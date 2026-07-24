import { NeedType } from "./need-type";

export interface Need {
    id: number;
    name: string;
    cost: number;
    quantity: number;
    type: NeedType;
}
