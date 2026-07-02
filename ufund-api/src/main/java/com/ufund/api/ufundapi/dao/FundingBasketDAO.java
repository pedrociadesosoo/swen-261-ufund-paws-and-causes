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
    
    /**
     * Creates a new {@linkplain FundingBasket fb}
     * @param fb {@link FundingBasket} the FundingBasket to be created
     * @return new {@link FundingBasket} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    FundingBasket createFundingBasket(FundingBasket fb) throws IOException;

    /**
     * 
     * @param id
     * @return
     * @throws IOException
     */
    FundingBasket deleteFundingBasket(int id) throws IOException;

    Funding



}
