import { Need } from "./need.model";

/**
 * Mirror of the backend FundingBasket model. `needs` is keyed by need id;
 * it is an index-signature object (not a Map) because that is what JSON
 * deserializes to.
 */
export interface FundingBasket {
    id: number;
    needs: { [key: number]: Need };
    /** Username of the helper who owns this basket; set server-side from the X-Username header. */
    ownerUsername?: string;
}
