package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufund.api.ufundapi.dao.CheckoutDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;


@Tag("Service-tier")
public class CheckoutServiceTest {
    private CheckoutServiceImpl testService;
    private CheckoutDAO mockDao;

    @BeforeEach
    public void setupCheckoutServiceTest() throws IOException{
        mockDao = mock(CheckoutDAO.class);
        testService = new CheckoutServiceImpl(mockDao);
    }

    private FundingBasket sampleBasket(){
        Map<Integer, Need> testMap = new TreeMap<Integer,Need>();
        testMap.put(0, new Need(0, "Canned Soup", 2.50, 50, "food"));
        testMap.put(1, new Need(1, "Winter Coats", 50.0, 10, "clothing"));
        testMap.put(2, new Need(4, "Blankets", 15.00, 25, "clothing"));
        testMap.put(3, new Need(2, "Rice", 1.99, 100, "food"));

        FundingBasket testBasket = new FundingBasket(0, testMap);
        return testBasket;
    }

    private Checkout sampleCheckout(){
        return new Checkout(1, sampleBasket());
    }

    /* 
    @Test
    public void testTransitionExists() throws IOException{
        // setup test data
        int testId = 1;
        Checkout testCheckout = sampleCheckout();
        FundingBasket testBasket = sampleBasket();

        // force to return valid checkout
        when(mockDao.getCheckout(testId)).thenReturn(testCheckout);
        
        // verify returns checkout
        Checkout actualCheckout = testService.transitionBasketToCheckout(testBasket);
        assertEquals(sampleBasket(), actualCheckout);
    
    }

    @Test
    public void testTransitionNull() throws IOException{
        // setup test data
        int testId = 10;
        FundingBasket testBasket = sampleBasket();

        // force to return null
        when(mockDao.getCheckout(testId)).thenReturn(null);
        
        // verify returns checkout
        Checkout actualCheckout = testService.transitionBasketToCheckout(testBasket);
        assertEquals(sampleCheckout(), actualCheckout);
    
    }

    @Test
    public void testCompleteCancelCheckout() throws IOException{
        // setup test data
        int testId = 1;
        Checkout testCheckout = sampleCheckout();

        // force to return valid checkout
        when(mockDao.getCheckout(testId)).thenReturn(testCheckout);
        
        // verify returns checkout
        Checkout deletedCheckout = testService.completeCancelCheckout(testId);
        assertEquals(sampleCheckout(), deletedCheckout);
    }

    @Test
    public void testCompleteCancelCheckoutNull() throws IOException{
        // setup test data
        int testId = 10;
        
        // force to return valid checkout
        when(mockDao.getCheckout(testId)).thenReturn(null);
        
        // verify returns checkout
        Checkout deletedCheckout = testService.completeCancelCheckout(testId);
        assertEquals(null, deletedCheckout);
    }
        */
}
