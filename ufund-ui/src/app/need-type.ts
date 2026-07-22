export enum NeedType {
    /** Corresponds to the SELECT option on the frontend that is used to validate data, 
     * and is needed here to align the types properly.
     */
    SELECT,

    /** A physical item being donated (food, clothing, supplies, etc.). */
    ITEM_DONATION,

    /** A direct monetary contribution toward a dollar goal. */
    MONETARY,

    /** A need for volunteer time or effort rather than money or goods. */
    VOLUNTEERING
}
