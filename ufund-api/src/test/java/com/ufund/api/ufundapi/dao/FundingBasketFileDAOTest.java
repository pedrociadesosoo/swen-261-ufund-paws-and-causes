package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;

@Tag("Persistence-tier")
public class FundingBasketFileDAOTest {

    private ObjectMapper mockMapper;
    private FundingBasketFileDAO fbdao;

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
    public void setupFBDAO() throws IOException {
        mockMapper = mock(ObjectMapper.class);
        when(mockMapper.readValue(any(File.class), eq(FundingBasket[].class)))
            .thenReturn(new FundingBasket[] { sampleFB() });
        fbdao = new FundingBasketFileDAO("data/fundingbaskets.json", mockMapper);
    }

    @Test
    void testGetFBReturnsFB() {
        FundingBasket fb = fbdao.getFundingBasket(1);

        assertEquals(1, fb.getId());
        assertEquals(3, fb.getNeeds().size());
    }

    @Test
    void testGetFBMissing() {
        FundingBasket fb = fbdao.getFundingBasket(99);

        assertNull(fb);
    }

    @Test
    void testGetFBArrayReturnsAll() {
        FundingBasket[] baskets = fbdao.getFundingBasketArray();

        assertEquals(1, baskets.length);
        assertEquals(1, baskets[0].getId());
    }

    @Test
    void testCreateFBAssignsNextId() throws IOException {
        FundingBasket created = fbdao.createFundingBasket(new FundingBasket(0, new HashMap<>()));

        assertEquals(2, created.getId());
        assertEquals(2, fbdao.getFundingBasketArray().length);
    }

    @Test
    void testCreateFBFail() throws IOException {
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(FundingBasket[].class));

        FundingBasket created = fbdao.createFundingBasket(new FundingBasket(0, new HashMap<>()));

        assertNull(created);
    }

    @Test
    void testDeleteFBReturnsTrue() throws IOException {
        boolean deleted = fbdao.deleteFundingBasket(1);

        assertEquals(true, deleted);
        assertNull(fbdao.getFundingBasket(1));
    }

    @Test
    void testDeleteFBMissingReturnsFalse() throws IOException {
        boolean deleted = fbdao.deleteFundingBasket(99);

        assertEquals(false, deleted);
    }

    @Test
    void testAddNeedFB() throws IOException {
        FundingBasket fb = fbdao.getFundingBasket(1);
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");

        boolean added = fbdao.addNeed(fb, newNeed);

        assertEquals(true, added);
        assertEquals(4, fb.getNeeds().size());
    }

    @Test
    void testAddNeedFBDuplicate() throws IOException {
        FundingBasket fb = fbdao.getFundingBasket(1);
        Need dupNeed = new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test");

        boolean added = fbdao.addNeed(fb, dupNeed);

        assertEquals(false, added);
        assertEquals(3, fb.getNeeds().size());
    }

    @Test
    void testAddNeedFBSaveFail() throws IOException {
        FundingBasket fb = fbdao.getFundingBasket(1);
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(FundingBasket[].class));

        assertThrows(IOException.class, () -> fbdao.addNeed(fb, newNeed));
    }

    @Test
    void testRemoveNeedFB() throws IOException {
        FundingBasket fb = fbdao.getFundingBasket(1);
        Need need = new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test");

        boolean removed = fbdao.removeNeed(fb, need);

        assertEquals(true, removed);
        assertEquals(2, fb.getNeeds().size());
    }

    @Test
    void testRemoveNeedFBMissing() throws IOException {
        FundingBasket fb = fbdao.getFundingBasket(1);
        Need need = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");

        boolean removed = fbdao.removeNeed(fb, need);

        assertEquals(false, removed);
        assertEquals(3, fb.getNeeds().size());
    }
}
