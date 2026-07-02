package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;

public interface  CheckoutService {

    public Checkout transitionFundingBasketToCheckout(FundingBasket basket) throws IOException;

    public Checkout completeCheckout() throws IOException;

    public Checkout cancelCheckout() throws IOException;
}
