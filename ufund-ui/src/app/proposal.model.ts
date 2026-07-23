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
    allVotes: Map<string, number>;
    status: string;

    [key: string]: any;
}
