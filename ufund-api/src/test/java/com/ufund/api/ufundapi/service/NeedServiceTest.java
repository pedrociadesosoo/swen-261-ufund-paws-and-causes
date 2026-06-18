package com.ufund.api.ufundapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.dao.NeedDAO;
import com.ufund.api.ufundapi.model.Need;

class NeedServiceTest {

    private NeedServiceImpl service;
    private NeedDAO mockDao;

    @BeforeEach
    void setup() {
        mockDao = mock(NeedDAO.class);
        service = new NeedServiceImpl(mockDao);
    }

    @Test
    void testGetAllNeedsReturnsAll() throws IOException {
        List<Need> needs = Arrays.asList(
            new Need(1, "Canned Soup", 2.50, 50, "food"),
            new Need(2, "Rice", 1.99, 100, "food"));
        when(mockDao.getAllNeeds()).thenReturn(needs);

        List<Need> result = service.getAllNeeds();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Canned Soup", result.get(0).getName());
        assertEquals(50, result.get(0).getQuantity());
        assertEquals("food", result.get(0).getType());
        verify(mockDao).getAllNeeds();
    }

    @Test
    void testGetAllNeedsWhenEmpty() throws IOException {
        when(mockDao.getAllNeeds()).thenReturn(Collections.emptyList());

        List<Need> result = service.getAllNeeds();

        assertTrue(result.isEmpty());
        verify(mockDao).getAllNeeds();
    }
}