package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Harikleia Sparakis
 */
@Tag("Model-Tier")
public class CheckoutTest {

    private FundingBasket testBasket;
    private Checkout testCheckout;
    
    /**
     * 
     */
    @BeforeEach
    public void setupCheckout(){

        Map<Integer, Need> testMap = new TreeMap<Integer,Need>();
        testMap.put(0, new Need(0, "Canned Soup", 2.50, 50, "food"));
        testMap.put(1, new Need(1, "Winter Coats", 50.0, 10, "clothing"));
        testMap.put(2, new Need(4, "Blankets", 15.00, 25, "clothing"));
        testMap.put(3, new Need(2, "Rice", 1.99, 100, "food"));

        testBasket = new FundingBasket(0, testMap);
    }

    /**
     * 
     */
    @Test
    public void testConstructor(){
        //test data already set up by setupCheckout()
        //test constructor 
        testCheckout = new Checkout(testBasket.getId(), testBasket);

        //verify that constructor's created object as expected
        assertEquals(testBasket.getNeeds().get(0), testCheckout.getNeeds().get(0));
        assertEquals(testBasket.getNeeds().get(1), testCheckout.getNeeds().get(1));
        assertEquals(testBasket.getNeeds().get(2), testCheckout.getNeeds().get(2));
        assertEquals(testBasket.getNeeds().get(3), testCheckout.getNeeds().get(3));

    }

    /**
     * 
     */
    @Test
    public void testCaluclateTotalCost(){
        //test data already set up by setupCheckout()
        //test constructor 
        testCheckout = new Checkout(testBasket.getId(), testBasket);
        double expectedCost = 1199;

        //call calculateTotalCost() method

        double actualCost = testCheckout.getTotalCost();

        // verify the cost returned is the correct value
        assertEquals(expectedCost, actualCost);
    }

}
