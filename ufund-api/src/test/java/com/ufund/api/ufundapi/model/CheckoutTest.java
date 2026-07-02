package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author Harikleia Sparakis
 */
@Tag("Model-Tier")
public class CheckoutTest {
    
    /**
     * 
     */
    @BeforeEach
    public void setupCheckup(){
        Need[] testNeeds = {
            new Need(0, null, 0, 0, null),
            new Need(1, null, 0, 0, null),
            new Need(2, null, 0, 0, null),
            new Need(3, null, 0, 0, null)
        };
    }

    /**
     * 
     */
    @Test
    public void testConstructorNeeds(){
        //set up test data
        Need[] testNeeds = {
            new Need(0, null, 0, 0, null),
            new Need(1, null, 0, 0, null),
            new Need(2, null, 0, 0, null),
            new Need(3, null, 0, 0, null)
        };

        //test constructor 
        Checkout testCheckout = new Checkout(testNeeds);

        //verify that constructor's created object as expected
        assertEquals(testNeeds[0], testCheckout.getNeeds()[0]);
        assertEquals(testNeeds[1], testCheckout.getNeeds()[1]);
        assertEquals(testNeeds[2], testCheckout.getNeeds()[2]);
        assertEquals(testNeeds[3], testCheckout.getNeeds()[3]);

    }
    
    /**
     * 
     */
    @Test
    public void testConstructorBasket(){

    }

    /**
     * 
     */
    @Test
    public void testToString() {
        // setup testing data
        Need[] testNeeds = {
            new Need(0, null, 0, 0, null),
            new Need(1, null, 0, 0, null),
            new Need(2, null, 0, 0, null),
            new Need(3, null, 0, 0, null)
        };
        Checkout testCheckout = new Checkout(testNeeds);
        String expectedString = "";

        // call toString() method
        String actualString = testCheckout.toString();

        // verify returned string is formatted properly
        assertEquals(expectedString,actualString);
    }

    /**
     * 
     */
    @Test
    public void testCaluclateTotalCost(){
        //setup test data
        Need[] testNeeds = {
            new Need(0, null, 0, 0, null),
            new Need(1, null, 0, 0, null),
            new Need(2, null, 0, 0, null),
            new Need(3, null, 0, 0, null)
        };
        Checkout testCheckout = new Checkout(testNeeds);
        double expectedCost = 0;

        //call calculateTotalCost() method

        double actualCost = testCheckout.getTotalCost();

        // verify the cost returned is the correct value
        assertEquals(expectedCost, actualCost);
    }

}
