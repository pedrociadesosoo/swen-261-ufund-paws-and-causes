package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.dao.FundingBasketDAO;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.service.NeedService;

@Tag("Controller-tier")
public class FundingBasketControllerTest {
    private FundingBasketController fbCont;
    private FundingBasketDAO fbDao;
    private NeedService needService;

    
    private Need[] sampleNeeds() {
        return new Need[] {
                new Need(1, "Canned Soup", 2.50, 50, "food"),
                new Need(2, "Rice", 1.99, 100, "food"),
                new Need(3, "Blankets", 15.00, 25, "clothing")
        };
    }

    private FundingBasket sampleFB(){

        Need[] needs = sampleNeeds();
        Map<Integer, Need> needMap = new HashMap<>();

        for (Need need : needs){
            needMap.put(need.getId(), need);
        }
        FundingBasket fb = new FundingBasket(1);
        fb.setMap(needMap);
        return fb;
    }

    @BeforeEach
    public void setupFBController(){
        fbDao = mock(FundingBasketDAO.class);
        needService = mock(NeedService.class);
        fbCont = new FundingBasketController(fbDao, needService);
    }

    @Test
    public void testCreateFB() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbDao.getFundingBasketArray()).thenReturn(new FundingBasket[0]);
        when(fbDao.createFundingBasket(1)).thenReturn(fb); 

        ResponseEntity<FundingBasket> response = fbCont.createFundingBasket(1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(fb, response.getBody());
    }
    
    @Test
    public void testCreateFBConflict() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbDao.getFundingBasketArray()).thenReturn(new FundingBasket[] {fb});

        ResponseEntity<FundingBasket> response = fbCont.createFundingBasket(1);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testDeleteFB() throws IOException{
        FundingBasket fb = sampleFB();
        when(fbDao.getFundingBasketArray()).thenReturn(new FundingBasket[] {fb} );
        when(fbDao.deleteFundingBasket(1)).thenReturn(true);

        ResponseEntity<Boolean> response = fbCont.deleteFundingBasket(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    
    }
    @Test
    public void testDeleteFBNotFound() throws IOException {
        FundingBasket fb = sampleFB();
        when(fbDao.getFundingBasket(1)).thenReturn(null);

        ResponseEntity<FundingBasket> response = fbCont.getFundingBasket(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void getNeedsFB() throws IOException{
        FundingBasket fb = sampleFB();
        Map<Integer, Need> needs = fb.getNeeds();
        when(fbDao.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Map<Integer, Need>> response = fbCont.getNeeds(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void addNeedFB() throws IOException{
        FundingBasket fb = sampleFB();
        Map<Integer,Need> needs = fb.getNeeds();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");

        when(fbDao.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Need> response = fbCont.addNeed(1, newNeed);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newNeed, response.getBody());

    }

    @Test
    public void addNeedPresentFB() throws IOException {
        FundingBasket fb = sampleFB();
        Map<Integer,Need> needs = fb.getNeeds();
        Need newNeed = new Need(3, "Blankets", 15.00, 25, "clothing");

        when(fbDao.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Need> response = fbCont.addNeed(1, newNeed);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void addNeedNonexistentFB(){

    }

    @Test
    public void deleteNeedFB() throws IOException{
        FundingBasket fb = sampleFB();
        Map<Integer,Need> needs = fb.getNeeds();
        Need deleteNeed = new Need(3, "Blankets", 15.00, 25, "clothing");

        when(fbDao.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Need> response = fbCont.removeNeed(1, deleteNeed);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deleteNeed, response.getBody());

    }

    @Test
    public void deleteNeedNonexistentNeed() throws IOException {

        FundingBasket fb = sampleFB();
        Map<Integer,Need> needs = fb.getNeeds();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");

        when(fbDao.getFundingBasket(1)).thenReturn(fb);

        ResponseEntity<Need> response = fbCont.removeNeed(1, newNeed);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }



}
