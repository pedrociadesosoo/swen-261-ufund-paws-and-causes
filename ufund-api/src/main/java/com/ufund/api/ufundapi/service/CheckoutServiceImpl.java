package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Map;

import com.ufund.api.ufundapi.dao.CheckoutDAO;
import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;


public class CheckoutServiceImpl implements CheckoutService{

    private CheckoutDAO checkoutDAO;
    private NeedDAO needDAO;

    public CheckoutServiceImpl(CheckoutDAO checkoutDAO, NeedDAO needDAO) throws IOException {
        this.checkoutDAO = checkoutDAO;
        this.needDAO = needDAO;
    }

    @Override
    public Checkout transitionBasketToCheckout(FundingBasket basket) throws IOException {
        if (checkoutDAO.getCheckout(basket.getId()) != null) {
            checkoutDAO.createCheckout(basket);
        }
        return checkoutDAO.getCheckout(basket.getId());
    }

    @Override
    public Checkout completeCheckout(int checkoutId) throws IOException {
        Checkout completedCheckout = checkoutDAO.getCheckout(checkoutId);
        if (  completedCheckout != null) {
            Map<Integer, Need> needsToDelete = checkoutDAO.getCheckout(checkoutId).getNeeds();
            for(Need need : needsToDelete.values()){
                needDAO.deleteNeed(need.getId());
            }
            checkoutDAO.deleteCheckout(checkoutId);
            return completedCheckout;
        } else {
            return null;
        }
    }

    @Override
    public Checkout cancelCheckout(int checkoutId) throws IOException {
        Checkout cancelledCheckout = checkoutDAO.getCheckout(checkoutId);
        if ( checkoutDAO.getCheckout(checkoutId) != null) {
            checkoutDAO.deleteCheckout(checkoutId);
            return cancelledCheckout;
        } else {
            return null;
        }
    }
}
