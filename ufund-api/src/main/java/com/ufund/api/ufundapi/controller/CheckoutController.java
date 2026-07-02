package com.ufund.api.ufundapi.controller;

import java.util.logging.Logger;

import com.ufund.api.ufundapi.service.CheckoutService;

public class CheckoutController {

    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    

}
