package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufund.api.ufundapi.dao.CheckoutDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Checkout;
import com.ufund.api.ufundapi.model.FundingBasket;


@Tag("Service-tier")
public class CheckoutServiceTest {
    private CheckoutServiceImpl service;
    private CheckoutDAO mockDao;

    @BeforeEach
    public void setupCheckoutServiceTest() throws IOException{
        mockDao = mock(CheckoutDAO.class);
        service = new CheckoutServiceImpl(mockDao);
    }



}
