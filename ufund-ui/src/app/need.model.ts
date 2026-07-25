import { NeedType } from "./need-type";
import { Organization } from "./organization";

export interface Need {
    id: number;
    name: string;
    cost: number;
    quantity: number;
    type: NeedType;
    organization: Organization;
}
