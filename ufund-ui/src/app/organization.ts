import { Need } from "./need.model";

export interface Organization {
    name: string;
    description: string;
    needs: Map<number, Need>;
}