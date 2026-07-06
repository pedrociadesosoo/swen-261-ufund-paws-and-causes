package com.ufund.api.ufundapi.dao;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;

public interface CheckoutDAO {

    //#region get methods

    public Checkout getCheckout(int id) throws IOException;
    
    //#endregion


    //#region creating methods

    public Checkout createCheckout(FundingBasket basket) throws IOException;

    //#endregion


    //#region deleting methods

    public boolean deleteCheckout(int id) throws IOException;

    //#endregion
    
}
