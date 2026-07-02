package com.ufund.api.ufundapi.dao;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.FundingBasket;;

public interface FundingBasketDAO {

    /**
     * returns {@linkplain Fundingbasket fb} 
     * @param id {@link int id} the id of the FundingBasket
     * @return {@link FundingBasket fb} the Funding basket with the same id
     */
    FundingBasket getFundingBasket(int id);

    FundingBasket[] getFundingBasketArray();
    
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

}
