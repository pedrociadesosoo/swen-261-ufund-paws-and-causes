package com.ufund.api.ufundapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;

@Tag("Persistence-tier")
public class FundingBasketFileDAOTest {

    private ObjectMapper mockMapper;
    private FundingBasketFileDAO fbdao;

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
    public void setupFBDAO() throws IOException {
        mockMapper = mock(ObjectMapper.class);
        when(mockMapper.readValue(any(File.class), eq(FundingBasket[].class)))
            .thenReturn(new FundingBasket[0]);
        fbdao = new FundingBasketFileDAO("data/fundingbaskets.json", mockMapper);
    }

    @Test
    void testCreateFBReturnsFB (){
        FundingBasket fb = sampleFB();

        FundingBasket created = fbdao.createFundingBasket(fb.getId());

        assertEquals(fb.getId(), created.getId());
        assertEquals(fb.getNeeds().size(), created.getNeeds().size());

    }

    @Test
    void testCreateFBFail() throws IOException {
        FundingBasket fb = sampleFB();
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(FundingBasket[].class));

        FundingBasket created = fbdao.createFundingBasket(fb.getId());

        assertEquals(null, created);
    }

    @Test
    void testDeleteFBReturnsFB() throws IOException{
        FundingBasket fb = sampleFB();
        FundingBasket deleted = fbdao.deleteFundingBasket(fb.getId());

        assertEquals(fb, deleted);
        
    }

    @Test
    void testDeleteFBFail() throws IOException{
        FundingBasket fb = sampleFB();
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(FundingBasket[].class));
        
        FundingBasket deleted = fbdao.deleteFundingBasket(fb.getId());
        assertEquals(null, deleted);

    }

    @Test
    void testGetNeedsReturnsAll() throws IOException{
        FundingBasket fb = sampleFB();

        Map<Integer,Need> needs = fbdao.getFundingBasketNeeds(fb.getId());

        assertEquals(fb.getNeeds(), needs);

    }

    @Test
    void testGetNeedsEmpty(){


    }

    @Test
    void testGetNeedsFail() throws IOException{
        FundingBasket fb = sampleFB();
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(FundingBasket[].class));

        Map<Integer,Need> needs = fbdao.getFundingBasketNeeds(fb.getId());
        assertNull(needs);

    }

    @Test
    void testAddNeedFB(){
        FundingBasket fb = sampleFB();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");

        Need need = fbdao.addNeed(fb, newNeed);
        assertEquals(newNeed.getName(), need.getName());
        assertEquals(newNeed.getId(), need.getId());
    }

    @Test
    void testAddNeedFBFail() throws IOException{
        FundingBasket fb = sampleFB();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(FundingBasket[].class));

        Need need = fbdao.addNeed(fb, newNeed);
        assertEquals(null, need);

    }

    @Test
    void testRemoveNeedFB(){

    }

    @Test
    void testRemoveNeedFBFail(){

    }
}
