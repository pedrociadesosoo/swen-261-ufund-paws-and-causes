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
    public Checkout transitionBasketToCheckout(FundingBasket basket) throws IOException {
        if (checkoutDAO.getCheckout(basket.getId()) != null) {
            checkoutDAO.createCheckout(basket);
        }
        return checkoutDAO.getCheckout(basket.getId());
    }

    @Override
    public Checkout completeCheckout(int checkoutId) throws IOException {
        if ( checkout != null) {
            checkoutDAO.deleteCheckout(checkoutId);
            return checkout;
        } else {
            return null;
        }
    }

    @Override
    public Checkout cancelCheckout(int checkoutId) throws IOException{
        if ( checkout != null) {
            checkoutDAO.deleteCheckout(checkoutId);
            return checkout;
        } else {
            return null;
        }
    } 

}
