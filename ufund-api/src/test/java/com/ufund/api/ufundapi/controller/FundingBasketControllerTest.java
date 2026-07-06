package com.ufund.api.ufundapi.controller;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.dao.FundingBasketDAO;
import com.ufund.api.ufundapi.service.NeedService;

@Tag("Controller-tier")
public class FundingBasketControllerTest {
    private FundingBasketController fbCont;
    private FundingBasketDAO fbDao;
    private NeedService needService;

    @BeforeEach
    public void setupFBController(){
        fbDao = mock(FundingBasketDAO.class);
        needService = mock(NeedService.class);
        fbCont = new FundingBasketController(fbDao, needService);
    }

    @Test
    public void testCreateFB(){

    }

    @Test
    public void testDeleteFB(){

    }

    @Test
    public void getNeedsFB(){

    }

    @Test
    public void addNeedFB(){

    }

    @Test
    public void addNeedPresentFB(){

    }

    @Test
    public void addNeedNonexistentFB(){

    }

    @Test
    public void deleteNeedFB(){

    }

    @Test
    public void deleteNeedNonexistentFB(){

    }



}
