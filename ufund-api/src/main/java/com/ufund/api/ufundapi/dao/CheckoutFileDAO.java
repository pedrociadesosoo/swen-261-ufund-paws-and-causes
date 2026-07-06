package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;

@Component
public class CheckoutFileDAO implements CheckoutDAO {
    private static final Logger LOG = Logger.getLogger(CheckoutFileDAO.class.getName());
    
    private Map<Integer, Checkout> checkouts = new TreeMap<>();
    private ObjectMapper objectMapper;
    private static int nextId; 
    private String filename;
    
    public CheckoutFileDAO(@Value("${checkouts.file}") String filename, ObjectMapper objectMapper) 
        throws IOException
    {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private boolean save() throws IOException {
        Checkout[] fbArray = getCheckoutArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), fbArray);
        return true;
    }

    private Checkout[] getCheckoutArray() {
        ArrayList<Checkout> checkoutArrayList = new ArrayList<>();

        for (Checkout checkout : checkouts.values()) {
            checkoutArrayList.add(checkout);
        }

        Checkout[] checkoutArray = (Checkout[]) checkoutArrayList.toArray();
        return checkoutArray;
    }

    private boolean load() throws IOException {
        checkouts = new TreeMap<>();
        nextId = 0;

        Checkout[] checkoutArray = objectMapper.readValue(new File(filename), Checkout[].class);
        
        for (Checkout checkout : checkoutArray) {
            checkouts.put(checkout.getId(), checkout);
            if (checkout.getId() >= nextId)
                nextId = checkout.getId();
        }
        ++nextId;
        return true;
    }

    @Override
    public Checkout getCheckout(int id) throws IOException {
        synchronized(checkouts){
            if (checkouts.containsKey(id)) {
                return checkouts.get(id);
            } else {
                return null;
            }
        }
    }

    @Override
    public Checkout createCheckout(FundingBasket basket) throws IOException {
       synchronized (checkouts) {
           Checkout newCheckout = new Checkout(basket.getId(), basket);
           checkouts.put(basket.getId(), newCheckout);
           save();
           return newCheckout;
       }
    }

    @Override
    public boolean deleteCheckout(int id) throws IOException {
        synchronized (checkouts) {
            if(checkouts.containsKey(id)){
                checkouts.remove(id);
                return save();
            } else {
                return false;
            }            
        } 
    }
}