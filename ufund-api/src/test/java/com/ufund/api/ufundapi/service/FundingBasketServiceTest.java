package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ufund.api.ufundapi.dao.FundingBasketDAO;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;

@Tag("Service-tier")
public class FundingBasketServiceTest {
    FundingBasketImpl testService;
    FundingBasketDAO mockDAO;

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
    public void setup(){
        mockDAO = mock(FundingBasketDAO.class);
        testService = new FundingBasketImpl(mockDAO);
    }

    @Test
    public void testGetFundingBasket() throws IOException {
        testService.getFundingBasket(1);
        verify(mockDAO).getFundingBasket(1);
    }

    @Test
    public void testGetFundingBasketArray() throws IOException {
        testService.getFundingBasketArray();
        verify(mockDAO).getFundingBasketArray();
    }

    @Test
    public void testCreateFundingBasket() throws IOException {
        FundingBasket testBasket = sampleFB();
        testService.createFundingBasket(testBasket);
        verify(mockDAO).createFundingBasket(testBasket);
    }

    @Test
    public void testDeleteFundingBasket() throws IOException {
        testService.deleteFundingBasket(1);
        verify(mockDAO).deleteFundingBasket(1);
    }

    @Test
    public void testAddNeed() throws IOException {
        FundingBasket testBasket = sampleFB();
        Need testNeed = new Need(0, "test", 0, 0, NeedType.ITEM_DONATION, "test");
        testService.addNeed(testBasket, testNeed);
        verify(mockDAO).addNeed(testBasket, testNeed);
    }

    @Test
    public void testRemoveNeed() throws IOException {
        FundingBasket testBasket = sampleFB();
        Need testNeed = new Need(0, "test", 0, 0, NeedType.ITEM_DONATION, "test");
        testService.removeNeed(testBasket, testNeed);
        verify(mockDAO).removeNeed(testBasket, testNeed);
    }
}
