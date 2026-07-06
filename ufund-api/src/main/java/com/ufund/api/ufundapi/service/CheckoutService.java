package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;

public interface  CheckoutService {

    public Checkout transitionBasketToCheckout(FundingBasket basket) throws IOException;

    public Checkout completeCancelCheckout(int checkoutId) throws IOException;
}
