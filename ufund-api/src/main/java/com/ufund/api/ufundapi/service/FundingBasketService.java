package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;


public interface FundingBasketService {

    /**
     * returns {@linkplain FundingBasket fb}
     * @param id {@link int id} the id of the FundingBasket
     * @return {@link FundingBasket fb} the Funding basket with the same id
     */
    FundingBasket getFundingBasket(int id) throws IOException;

    FundingBasket[] getFundingBasketArray() throws IOException;

    /**
     * Creates a new {@linkplain FundingBasket fb}
     * @param fb {@link FundingBasket fb} the FundingBasket to be created
     * @return new {@link FundingBasket fb} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    FundingBasket createFundingBasket(FundingBasket fb) throws IOException;

    /**
     * Deletes an existing {@linkplain FundingBasket fb}
     * @param id {@link int id} the id of the FundingBasket for deletion
     * @return true if successful, false if failure
     * @throws IOException if an issue with storage access occurs
     */
    boolean deleteFundingBasket(int id) throws IOException;

    boolean addNeed(FundingBasket fb, Need need) throws IOException;

    boolean removeNeed(FundingBasket fb, Need need) throws IOException;

}
