/**
 * A proposal for a new need, submitted by a helper and pending manager review.
 */
export interface Proposal {
    id: number;
    name: string;
    cost: number;
    quantity: number;
    type: string;
    username: string;
    organization: string;
    creationDate: string;
    lastEdited: string;
    votes: { [username: string]: number };
}
