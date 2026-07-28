package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.service.FundingBasketService;
import com.ufund.api.ufundapi.service.NeedService;

@Tag("Controller-tier")
public class FundingBasketControllerTest {
    private FundingBasketController fbCont;
    private FundingBasketService fbService;
    private NeedService needService;


    private Need[] sampleNeeds() {
        return new Need[] {
                new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
                new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
                new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test")
        };
    }

    private FundingBasket sampleFB(){

        Need[] needs = sampleNeeds();
        Map<Integer, Need> needMap = new HashMap<>();

        for (Need need : needs){
            needMap.put(need.getId(), need);
        }
        return new FundingBasket(1, needMap);

        
    }

    @BeforeEach
    public void setupFBController(){
        fbService = mock(FundingBasketService.class);
        needService = mock(NeedService.class);
        fbCont = new FundingBasketController(fbService, needService);
    }

    @Test
    public void testGetFB() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbService.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<FundingBasket> response = fbCont.getFundingBasket(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fb, response.getBody());
    }

    @Test
    public void testGetFBNotFound() throws IOException {
        when(fbService.getFundingBasket(1)).thenReturn(null);

        ResponseEntity<FundingBasket> response = fbCont.getFundingBasket(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetFBHandleException() throws IOException {
        when(fbService.getFundingBasket(1)).thenThrow(new IOException("read failed"));

        ResponseEntity<FundingBasket> response = fbCont.getFundingBasket(1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateFB() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbService.createFundingBasket(fb)).thenReturn(fb);

        ResponseEntity<FundingBasket> response = fbCont.createFundingBasket("helper1", fb);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(fb, response.getBody());
        assertEquals("helper1", fb.getOwnerUsername());
    }

    @Test
    public void testCreateFBFail() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbService.createFundingBasket(fb)).thenReturn(null);

        ResponseEntity<FundingBasket> response = fbCont.createFundingBasket("helper1", fb);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Requests with no X-Username header can't create a basket for anyone.
     */
    @Test
    public void testCreateFBForbiddenWithNoUsername() throws IOException {
        FundingBasket fb = sampleFB();

        ResponseEntity<FundingBasket> response = fbCont.createFundingBasket(null, fb);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(fbService, never()).createFundingBasket(any());
    }

    /**
     * tests that createFundingBasket returns HttpStatus.INTERNAL_SERVER_ERROR when IOException occurs
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testCreateFBException() throws IOException {
        //set up test data
        FundingBasket testFB = sampleFB();

        //force throw exception
        doThrow(new IOException()).when(fbService).createFundingBasket(testFB);

        //verify response is HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<FundingBasket> response = fbCont.createFundingBasket("helper1", testFB);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetFBArray() throws IOException {
        FundingBasket[] baskets = new FundingBasket[] { sampleFB(), new FundingBasket(2, new HashMap<>()) };
        when(fbService.getFundingBasketsByOwner("helper1")).thenReturn(baskets);

        ResponseEntity<FundingBasket[]> response = fbCont.getFundingBasketArray("helper1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(baskets, response.getBody());
    }

    /**
     * Requests with no X-Username header can't list anyone's baskets, which
     * is the whole point of the per-owner fix: no header means no baskets.
     */
    @Test
    public void testGetFBArrayForbiddenWithNoUsername() throws IOException {
        ResponseEntity<FundingBasket[]> response = fbCont.getFundingBasketArray(null);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(fbService, never()).getFundingBasketsByOwner(any());
    }

    /**
     * tests that getFundingBasketArray returns HttpStatus.INTERNAL_SERVER_ERROR 
     * when IOException occurs
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testGetFBArrayException() throws IOException {
        //set up test data
        String testUsername = "test";

        //force throw exception
        doThrow(new IOException()).when(fbService).getFundingBasketsByOwner(testUsername);

        //verify response is HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<FundingBasket[]> response = fbCont.getFundingBasketArray(testUsername);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteFB() throws IOException {
        when(fbService.deleteFundingBasket(1)).thenReturn(true);

        ResponseEntity<Boolean> response = fbCont.deleteFundingBasket(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testDeleteFBNotFound() throws IOException {
        when(fbService.deleteFundingBasket(99)).thenReturn(false);

        ResponseEntity<Boolean> response = fbCont.deleteFundingBasket(99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    /**
     * tests that deleteFundingBasket returns HttpStatus.INTERNAL_SERVER_ERROR 
     * when IOException occurs
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testDeleteFBException() throws IOException {
        //set up test data
        int testId = 1;

        //force throw exception
        doThrow(new IOException()).when(fbService).deleteFundingBasket(testId);

        //verify response is HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Boolean> response = fbCont.deleteFundingBasket(testId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddNeedFB() throws IOException {
        FundingBasket fb = sampleFB();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");

        when(fbService.getFundingBasket(1)).thenReturn(fb);
        when(needService.getNeedById(4)).thenReturn(newNeed);
        when(fbService.addNeed(fb, newNeed)).thenReturn(true);

        ResponseEntity<Boolean> response = fbCont.addNeed(1, 4);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testAddNeedPresentFB() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbService.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Boolean> response = fbCont.addNeed(1, 3);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(fbService, never()).addNeed(any(), any());
    }

    @Test
    public void testAddNeedNonexistentFB() throws IOException {
        when(fbService.getFundingBasket(99)).thenReturn(null);

        ResponseEntity<Boolean> response = fbCont.addNeed(99, 4);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(fbService, never()).addNeed(any(), any());
    }

    @Test
    public void testAddNeedNonexistentNeed() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbService.getFundingBasket(1)).thenReturn(fb);
        when(needService.getNeedById(99)).thenReturn(null);

        ResponseEntity<Boolean> response = fbCont.addNeed(1, 99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(fbService, never()).addNeed(any(), any());
    }

    /**
     * tests that addNeed returns HttpStatus.INTERNAL_SERVER_ERROR when IOException occurs
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testAddNeedFBException() throws IOException {
        //set up test data
        int testFBId = 1;
        int testNeedId = 1;

        //force throw exception
        doThrow(new IOException()).when(fbService).getFundingBasket(testFBId);

        //verify response is HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Boolean> response = fbCont.addNeed(testFBId, testNeedId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveNeedFB() throws IOException {
        FundingBasket fb = sampleFB();
        Need need = new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test");

        when(fbService.getFundingBasket(1)).thenReturn(fb);
        when(needService.getNeedById(3)).thenReturn(need);
        when(fbService.removeNeed(fb, need)).thenReturn(true);

        ResponseEntity<Boolean> response = fbCont.removeNeed(1, 3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testRemoveNeedNotInBasket() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbService.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Boolean> response = fbCont.removeNeed(1, 4);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(fbService, never()).removeNeed(any(), any());
    }

    @Test
    public void testRemoveNeedNonexistentFB() throws IOException {
        when(fbService.getFundingBasket(99)).thenReturn(null);

        ResponseEntity<Boolean> response = fbCont.removeNeed(99, 3);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(fbService, never()).removeNeed(any(), any());
    }

    /**
     * tests that removeNeed returns HttpStatus.INTERNAL_SERVER_ERROR when IOException occurs
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testRemoveNeedFBException() throws IOException {
        //set up test data
        int testFBId = 1;
        int testNeedId = 1;

        //force throw exception
        doThrow(new IOException()).when(fbService).getFundingBasket(testFBId);

        //verify response is HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Boolean> response = fbCont.removeNeed(testFBId, testNeedId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * tests that checkout returns HttpStatus.INTERNAL_SERVER_ERROR when IOException occurs
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testCheckoutException() throws IOException {
        //set up test data
        int testId = 1;

        //force throw exception
        doThrow(new IOException()).when(fbService).getFundingBasket(testId);

        //verify response is HttpStatus.INTERNAL_SERVER_ERROR
        ResponseEntity<Boolean> response = fbCont.checkout(testId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * tests that checkout returns HttpStatus.NOT_FOUND when Fundingbasket is null
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testCheckoutNull() throws IOException {
        //set up test data
        int testId = 1;

        //force return null
        when(fbService.getFundingBasket(testId)).thenReturn(null);

        //verify response is HttpStatus.NOTFOUND
        ResponseEntity<Boolean> response = fbCont.checkout(testId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * tests that checkout returns HttpStatus.OK when Fundingbasket is valid
     * @throws IOException when underlying storage issue occurs
     */
    @Test
    public void testCheckoutExists() throws IOException {
        //set up test data
        int testId = 1;
        FundingBasket testBasket = sampleFB();

        //force return valid basket
        when(fbService.getFundingBasket(testId)).thenReturn(testBasket);

        //verify response is HttpStatus.OK
        ResponseEntity<Boolean> response = fbCont.checkout(testId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
