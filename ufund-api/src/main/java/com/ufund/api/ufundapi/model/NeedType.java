package com.ufund.api.ufundapi.model;

/**
 * The fixed set of categories a {@linkplain Need need} can belong to.
 */
public enum NeedType {
    /** A physical item being donated (food, clothing, supplies, etc.). */
    ITEM_DONATION,

    /** A direct monetary contribution toward a dollar goal. */
    MONETARY,

    /** A need for volunteer time or effort rather than money or goods. */
    VOLUNTEERING
}
