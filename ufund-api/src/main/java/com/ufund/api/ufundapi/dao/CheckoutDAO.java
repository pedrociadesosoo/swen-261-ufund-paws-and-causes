package com.ufund.api.ufundapi.dao;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.Need;

public interface CheckoutDAO {

    //#region get methods

    public Checkout getCheckout() throws IOException;
    public Need[] getCheckoutNeeds() throws IOException;
    public double getCheckoutTotalCost() throws IOException;
    
    //#endregion


    //#region creating methods

    /**
     * Creates a new {@linkplain Checkout checkout} from FundingBasket object
     * @param FundingBasket {@link FundingBasket basket} the need to be created
     * @return new {@link Checkout checkout} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    //public Checkout createCheckout(FundingBasket basket) throws IOException;

    /**
     * Creates a new {@linkplain Checkout checkout} from Needs array
     * @param needs {@link Need[] needs} the need to be created
     * @return new {@link Checkout checkout} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    public Checkout createCheckout(Need[] needs) throws IOException;

    /**
     * Creates a new {@linkplain Checkout checkout} from Needs list
     * @param needs {@link List<Need> needs} the need to be created
     * @return new {@link Checkout checkout} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    public Checkout createCheckout(List<Need> needs) throws IOException;

    //#endregion


    //#region deleting methods

    /**
     * Deletes {@linkplain Checkout checkout} the current checkout
     * @return true if successful, false if otherwise
     * @throws IOException if an issue with storage access occurs
     */
    public boolean deleteCheckout() throws IOException;

    //#endregion
    
}
