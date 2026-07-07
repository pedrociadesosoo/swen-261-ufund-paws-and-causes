package com.ufund.api.ufundapi.service;

import java.io.IOException;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.dao.FundingBasketDAO;


@Service
public class FundingBasketImpl implements FundingBasketService {

    private FundingBasketDAO fundingBasketDAO;

    public FundingBasketImpl(FundingBasketDAO fundingBasketDAO) {
        this.fundingBasketDAO = fundingBasketDAO;
    }

    /**
     * returns {@linkplain FundingBasket fb}
     * @param id {@link int id} the id of the FundingBasket
     * @return {@link FundingBasket fb} the Funding basket with the same id
     */
    public FundingBasket getFundingBasket(int id) throws IOException {
        return fundingBasketDAO.getFundingBasket(id);
    }

    public FundingBasket[] getFundingBasketArray() throws IOException {
        return fundingBasketDAO.getFundingBasketArray();
    }

    /**
     * Creates a new {@linkplain FundingBasket fb}
     * @param fb {@link FundingBasket fb} the FundingBasket to be created
     * @return new {@link FundingBasket fb} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    public FundingBasket createFundingBasket(FundingBasket fb) throws IOException {
        return fundingBasketDAO.createFundingBasket(fb);
    }

    /**
     * Deletes an existing {@linkplain FundingBasket fb}
     * @param id {@link int id} the id of the FundingBasket for deletion
     * @return true if successful, false if failure
     * @throws IOException if an issue with storage access occurs
     */
    public boolean deleteFundingBasket(int id) throws IOException {
        return fundingBasketDAO.deleteFundingBasket(id);
    }

    public boolean addNeed(FundingBasket fb, Need need) throws IOException {
        return fundingBasketDAO.addNeed(fb, need);
    }

    public boolean removeNeed(FundingBasket fb, Need need) throws IOException {
        return fundingBasketDAO.removeNeed(fb, need);
    }
}
