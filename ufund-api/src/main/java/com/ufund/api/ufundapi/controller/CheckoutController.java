package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.service.CheckoutService;

public class CheckoutController {

    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }  

    @PostMapping("")
    public ResponseEntity<Checkout> transitionBasketToCheckout(FundingBasket basket) 
    {
        LOG.info("POST /checkout/ " + basket.getId());
        try {
            Checkout existing = checkoutService.transitionBasketToCheckout(basket);
            if (existing != null ) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<Checkout>(
                    checkoutService.transitionBasketToCheckout(basket), 
                    HttpStatus.CREATED);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Checkout> completeCheckout(int checkoutId) 
    {
        LOG.info("Delete /checkout/ " + checkoutId);
        try {
            Checkout confirmedCheckout = checkoutService.completeCancelCheckout(checkoutId);
            if (confirmedCheckout != null) 
                return new ResponseEntity<Checkout>(confirmedCheckout, HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Checkout> cancelCheckout(int checkoutId) 
    {
        LOG.info("Delete /checkout/ " + checkoutId);
        try {
            Checkout confirmedCheckout = checkoutService.completeCancelCheckout(checkoutId);
            if (confirmedCheckout != null) 
                return new ResponseEntity<Checkout>(confirmedCheckout, HttpStatus.OK);
            else 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}