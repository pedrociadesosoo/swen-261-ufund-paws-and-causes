package com.ufund.api.ufundapi.dao;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Checkout;

public class CheckoutFileDAO {
    private static final Logger LOG = Logger.getLogger(CheckoutFileDAO.class.getName());
    
    private Map<Integer, Checkout> checkout = new TreeMap<>();
    private ObjectMapper objectMapper;
    private static int nextId; 
    private String filename; 


}
