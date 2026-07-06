package com.ufund.api.ufundapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;

@Tag("Persistence-tier")
class CheckoutFileDAOTest {
    private CheckoutFileDAO testCheckoutFileDAO;
    private Checkout[] testCheckouts;
    ObjectMapper mockObjectMapper;
    private Map<Integer, Checkout> mockCheckouts;
    
    @BeforeEach
    public void setupCheckoutFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testCheckouts = sampleCheckout();
        mockCheckouts = mock(Map.class);

        when(mockObjectMapper
            .readValue(new File("test.txt"),Checkout[].class))
                .thenReturn(testCheckouts);
        testCheckoutFileDAO = new CheckoutFileDAO("test.txt",mockObjectMapper);     
    }

    private Checkout[] sampleCheckout(){
        Map<Integer, Need> testMap = new TreeMap<Integer,Need>();
        testMap.put(0, new Need(0, "Canned Soup", 2.50, 50, "food"));
        testMap.put(1, new Need(1, "Winter Coats", 50.0, 10, "clothing"));
        testMap.put(2, new Need(4, "Blankets", 15.00, 25, "clothing"));
        testMap.put(3, new Need(2, "Rice", 1.99, 100, "food"));

        FundingBasket testBasket = new FundingBasket(0, testMap);

        testCheckouts = new Checkout[] {new Checkout(1, testBasket)};
        return testCheckouts;
    }

    @Test
    public void testGetCheckoutContainsId() throws IOException{
        //set up test specific data
        int testId = 1;

        //force to return valid checkout
        when(mockCheckouts.containsKey(testId)).thenReturn(true);

        //verify that proper checkout is returned as expected
        Checkout result = testCheckoutFileDAO.getCheckout(testId);
        assertEquals(testCheckouts[0], result);
    }

    @Test
    public void testGetCheckoutFail() throws IOException{
        //set up test data
        int testId = 10;

        //force to return null
        when(mockCheckouts.containsKey(testId)).thenReturn(false);

        //verify that null is returned
        Checkout result = testCheckoutFileDAO.getCheckout(testId);
        assertEquals(null, result);
    }

    /*@Test
    public void testCreateCheckoutReturnsCheckout() throws IOException{
        //set up test specific data
        Map<Integer, Need> newMap = new TreeMap<Integer,Need>();
        newMap.put(0, new Need(12, "Canned Soup", 2.50, 20, "food"));
        Checkout newCheckout = new Checkout(1, new FundingBasket(1, newMap));
        
        //verify that the checkout is returned
        Checkout actualCheckout = testCheckoutFileDAO.createCheckout(new FundingBasket(1, newMap));
        assertEquals(newCheckout, actualCheckout);        
    }   */
    
    @Test
    public void testDeleteNeedFalse() throws IOException {
        //setting up test data
        int checkoutTestId = 10;

        //force to return false
        when(mockCheckouts.containsKey(checkoutTestId)).thenReturn(false);
        
        //verify result is false
        boolean result = testCheckoutFileDAO.deleteCheckout(checkoutTestId);
        assertEquals(false, result);
    }

    /*@Test
    public void testDeleteNeedExists() throws IOException {
        //setting up test data
        int checkoutTestId = 1;

        //force to return true
        when(mockCheckouts.containsKey(checkoutTestId)).thenReturn(true);
        
        //verify result is true
        boolean result = testCheckoutFileDAO.deleteCheckout(checkoutTestId);
        assertEquals(true, result);
    }*/
}
