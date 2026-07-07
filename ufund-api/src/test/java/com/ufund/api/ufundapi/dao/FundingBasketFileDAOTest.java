package com.ufund.api.ufundapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void testCreateFBReturnsFB() throws IOException {
        FundingBasket fb = sampleFB();

        FundingBasket created = fbdao.createFundingBasket(fb.getId());

        assertNotNull(created);
        assertEquals(1, created.getId());
        assertTrue(created.getNeeds().isEmpty());
    }

    @Test
    void testCreateFBFail() throws IOException {
        FundingBasket fb = sampleFB();
        doThrow(new IOException("write failed"))
                .when(mockMapper)
                .writeValue(any(File.class), any(FundingBasket[].class));

        FundingBasket created = fbdao.createFundingBasket(fb.getId());

        assertNull(created);
    }

    @Test
    void testDeleteFBReturnsFB() throws IOException {
        FundingBasket created = fbdao.createFundingBasket(1);

        FundingBasket deleted = fbdao.deleteFundingBasket(created.getId());

        assertNotNull(deleted);
        assertEquals(created.getId(), deleted.getId());
    }

    @Test
    void testDeleteFBFail() throws IOException {
        FundingBasket deleted = fbdao.deleteFundingBasket(999);
        assertNull(deleted);
    }

    @Test
    void testGetNeedsReturnsAll() throws IOException {
        FundingBasket created = fbdao.createFundingBasket(1);
        created.setMap(sampleFB().getNeeds());

        Map<Integer, Need> needs = fbdao.getFundingBasketNeeds(created.getId());

        assertNotNull(needs);
        assertEquals(3, needs.size());
        assertEquals("Canned Soup", needs.get(1).getName());
    }

    @Test
    void testGetNeedsEmpty() throws IOException {
        FundingBasket created = fbdao.createFundingBasket(1);

        Map<Integer, Need> needs = fbdao.getFundingBasketNeeds(created.getId());

        assertNotNull(needs);
        assertTrue(needs.isEmpty());
    }

    @Test
    void testGetNeedsFail() throws IOException {
        Map<Integer, Need> needs = fbdao.getFundingBasketNeeds(999);
        assertNull(needs);
    }

    @Test
    void testAddNeedFB() {
        FundingBasket fb = sampleFB();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");

        Need need = fbdao.addNeed(fb, newNeed);
        assertEquals(newNeed.getName(), need.getName());
        assertEquals(newNeed.getId(), need.getId());
    }

    @Test
    void testAddNeedFBFail() {
        FundingBasket fb = sampleFB();
        Need duplicateNeed = new Need(1, "Canned Soup", 2.50, 50, "food");

        Need need = fbdao.addNeed(fb, duplicateNeed);
        assertNull(need);
    }

    @Test
    void testRemoveNeedFB() {
        FundingBasket fb = sampleFB();
        Need existingNeed = new Need(1, "Canned Soup", 2.50, 50, "food");

        Need removed = fbdao.removeNeed(fb, existingNeed);
        assertNotNull(removed);
        assertEquals(existingNeed.getId(), removed.getId());
    }

    @Test
    void testRemoveNeedFBFail() {
        FundingBasket fb = sampleFB();
        Need missingNeed = new Need(99, "Missing", 5.00, 1, "other");

        Need removed = fbdao.removeNeed(fb, missingNeed);
        assertNull(removed);
    }
}
