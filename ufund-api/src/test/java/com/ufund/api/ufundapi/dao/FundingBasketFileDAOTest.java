package com.ufund.api.ufundapi.dao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        return new FundingBasket(1, needMap);
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

    }

    @Test
    void testCreateFBFail(){

    }

    @Test
    void testDeleteFBReturnsBoolean(){

    }

    @Test
    void tetsDeleteFBFail(){

    }

    @Test
    void testGetNeedsReturnsAll(){

    }

    @Test
    void testGetNeedsEmpty(){

    }

    @Test
    void testGetNeedsFail(){

    }

    @Test
    void testAddNeedFB(){

    }

    @Test
    void testAddNeedFBFail(){

    }

    @Test
    void testRemoveNeedFB(){

    }

    @Test
    void testRemoveNeedFBFail(){

    }
}
