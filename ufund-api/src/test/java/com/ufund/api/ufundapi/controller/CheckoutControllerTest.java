package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.CheckoutService;
import com.ufund.api.ufundapi.service.NeedService;

public class CheckoutControllerTest {
    private CheckoutController testController;
    private CheckoutService testService;

    @BeforeEach
    public void setupCheckoutController() {
        testService = mock(CheckoutService.class);
        testController = new CheckoutController(testService);
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

    @Test
    public void testTransitionException() throws IOException {
        //set up test Data
        FundingBasket testBasket = sampleBasket();

        //force exception to be thrown
        doThrow(new IOException()).when(testService).transitionBasketToCheckout(testBasket);

        //verify method returns HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Checkout> response = testController.transitionBasketToCheckout(testBasket);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }    

    @Test
    public void testTransitionExisting() throws IOException {
        //set up test Dat
        FundingBasket testBasket = sampleBasket();

        //force to return existing checkout
        when(testService.transitionBasketToCheckout(testBasket)).thenReturn(sampleCheckout());

        //verify method returns HttpStatus.CONFLICT
        ResponseEntity<Checkout> response = testController.transitionBasketToCheckout(testBasket);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testTransitionNull() throws IOException {
        //set up test Data
        FundingBasket testBasket = sampleBasket();

        //force to return null
        when(testService.transitionBasketToCheckout(testBasket)).thenReturn(null);

        //verify method returns HttpStatus.Created
        ResponseEntity<Checkout> response = testController.transitionBasketToCheckout(testBasket);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test 
    public void testCompleteException() throws IOException {
        //set up test Data
        Checkout testCheckout = sampleCheckout();
        int testId = testCheckout.getId();

        //force exception to be thrown
        doThrow(new IOException()).when(testService).completeCheckout(testId);

        //verify method returns HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Checkout> response = testController.completeCheckout(testId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCompleteCheckoutExists() throws IOException {
        //set up test Data
        Checkout testCheckout = sampleCheckout();
        int testId = testCheckout.getId();

        //force method to return valid checkout
        when(testService.completeCheckout(testId)).thenReturn(sampleCheckout());

        //verify method returns HttpStatus.OK
        ResponseEntity<Checkout> response = testController.completeCheckout(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCompleteCheckoutNull() throws IOException {
        //set up test Data
        Checkout testCheckout = sampleCheckout();
        int testId = testCheckout.getId();

        //force method to return valid checkout
        when(testService.completeCheckout(testId)).thenReturn(null);

        //verify method returns HttpStatus.NOT_FOUND
        ResponseEntity<Checkout> response = testController.completeCheckout(testId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test 
    public void testCancelCheckoutException() throws IOException{
        //set up test Data
        Checkout testCheckout = sampleCheckout();
        int testId = testCheckout.getId();

        //force exception to be thrown
        doThrow(new IOException()).when(testService).cancelCheckout(testId);
        //when(testService.cancelCheckout(testId)).thenThrow(new IOException("read failed"));

        //verify method returns HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Checkout> response = testController.cancelCheckout(testId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCancelCheckoutExists() throws IOException {
        //set up test Data
        Checkout testCheckout = sampleCheckout();
        int testId = testCheckout.getId();

        //force method to return valid checkout
        when(testService.cancelCheckout(testId)).thenReturn(sampleCheckout());

        //verify method returns HttpStatus.OK
        ResponseEntity<Checkout> response = testController.cancelCheckout(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCancelCheckoutNull() throws IOException {
        //set up test Data
        Checkout testCheckout = sampleCheckout();
        int testId = testCheckout.getId();

        //force method to return valid checkout
        when(testService.cancelCheckout(testId)).thenReturn(null);

        //verify method returns HttpStatus.NOT_FOUND
        ResponseEntity<Checkout> response = testController.cancelCheckout(testId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    
}
