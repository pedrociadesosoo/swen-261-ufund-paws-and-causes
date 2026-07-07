package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.dao.FundingBasketDAO;

/**
 * Business-tier implementation of {@linkplain FundingBasketService}.
 * Currently delegates straight to the DAO; basket business rules
 * (e.g. per-helper ownership checks) belong here as they are added.
 */
@Service
public class FundingBasketImpl implements FundingBasketService {

    private FundingBasketDAO fundingBasketDAO;

    public FundingBasketImpl(FundingBasketDAO fundingBasketDAO) {
        this.fundingBasketDAO = fundingBasketDAO;
    }

    /**
     * {@inheritDoc}
     */
    public FundingBasket getFundingBasket(int id) throws IOException {
        return fundingBasketDAO.getFundingBasket(id);
    }

    /**
     * {@inheritDoc}
     */
    public FundingBasket[] getFundingBasketArray() throws IOException {
        return fundingBasketDAO.getFundingBasketArray();
    }

    /**
     * {@inheritDoc}
     */
    public FundingBasket[] getFundingBasketsByOwner(String ownerUsername) throws IOException {
        List<FundingBasket> owned = new ArrayList<>();
        for (FundingBasket fb : fundingBasketDAO.getFundingBasketArray()) {
            if (ownerUsername.equals(fb.getOwnerUsername())) {
                owned.add(fb);
            }
        }
        return owned.toArray(new FundingBasket[0]);
    }

    /**
     * {@inheritDoc}
     */
    public FundingBasket createFundingBasket(FundingBasket fb) throws IOException {
        return fundingBasketDAO.createFundingBasket(fb);
    }

    /**
     * {@inheritDoc}
     */
    public boolean deleteFundingBasket(int id) throws IOException {
        return fundingBasketDAO.deleteFundingBasket(id);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addNeed(FundingBasket fb, Need need) throws IOException {
        return fundingBasketDAO.addNeed(fb, need);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeNeed(FundingBasket fb, Need need) throws IOException {
        return fundingBasketDAO.removeNeed(fb, need);
    }
}
