package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.dao.CheckoutDAO;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;


public class CheckoutServiceImpl implements CheckoutService{

    private CheckoutDAO checkoutDAO;
    private Checkout checkout;

    public CheckoutServiceImpl(CheckoutDAO checkoutDAO) throws IOException {
        this.checkoutDAO = checkoutDAO;
    }

    @Override
    public Checkout transitionFundingBasketToCheckout(FundingBasket basket) throws IOException {
        if (checkoutDAO.getCheckout() != null) {
            checkoutDAO.createCheckout(basket);
        }
        return checkoutDAO.getCheckout();
    }

    @Override
    public Checkout completeCheckout() throws IOException {
        if ( checkout != null) {
            checkoutDAO.deleteCheckout();
            return checkout;
        } else {
            return null;
        }
    }

    @Override
    public Checkout cancelCheckout() throws IOException{
        if ( checkout != null) {
            checkoutDAO.deleteCheckout();
            return checkout;
        } else {
            return null;
        }
    } 

}
