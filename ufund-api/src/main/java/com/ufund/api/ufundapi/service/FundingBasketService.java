package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;

/**
 * Business-tier operations for {@linkplain FundingBasket funding baskets}.
 * Sits between the controller and the persistence tier.
 */
public interface FundingBasketService {

    /**
     * Retrieves the {@linkplain FundingBasket funding basket} with the given id.
     *
     * @param id the id of the FundingBasket
     * @return the FundingBasket with the matching id, or null if none exists
     * @throws IOException if an issue with storage occurs
     */
    FundingBasket getFundingBasket(int id) throws IOException;

    /**
     * Retrieves all {@linkplain FundingBasket funding baskets}.
     *
     * @return an array of every stored FundingBasket, empty if there are none
     * @throws IOException if an issue with storage occurs
     */
    FundingBasket[] getFundingBasketArray() throws IOException;

    /**
     * Creates a new {@linkplain FundingBasket fb}.
     *
     * @param fb the FundingBasket to be created
     * @return the created FundingBasket with its assigned id, null on failure
     * @throws IOException if an issue with storage occurs
     */
    FundingBasket createFundingBasket(FundingBasket fb) throws IOException;

    /**
     * Deletes an existing {@linkplain FundingBasket fb}.
     *
     * @param id the id of the FundingBasket to delete
     * @return true if deleted, false if no basket with that id exists
     * @throws IOException if an issue with storage occurs
     */
    boolean deleteFundingBasket(int id) throws IOException;

    /**
     * Adds a {@linkplain Need need} to a basket.
     *
     * @param fb the basket to add to
     * @param need the need to add
     * @return true if added, false if the need was already in the basket
     * @throws IOException if an issue with storage occurs
     */
    boolean addNeed(FundingBasket fb, Need need) throws IOException;

    /**
     * Removes a {@linkplain Need need} from a basket.
     *
     * @param fb the basket to remove from
     * @param need the need to remove
     * @return true if removed, false if the need was not in the basket
     * @throws IOException if an issue with storage occurs
     */
    boolean removeNeed(FundingBasket fb, Need need) throws IOException;

}
