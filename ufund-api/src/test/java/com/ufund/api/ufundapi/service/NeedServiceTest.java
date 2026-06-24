package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    /************* deleteNeed() unit tests, implemented by Harikleia Sparakis*************/

    /**
     * Tests if NeedService.deleteNeed() returns null when NeedDao.getNeedById() returns
     * null.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedNull() throws IOException {
        //set up test data
        Need testNeed = new Need(1, "Canned Soup", 2.50, 50, "food");

        //force mockDao.getNeedById() to return null 
        when(mockDao.getNeedById(testNeed.getId())).thenReturn(null);

        //check if method returns null
        Need deletedNeed = service.deleteNeed(testNeed.getId());
        assertEquals(testNeed, null);
    }

    /**
     * Tests if NeedService.deleteNeed() returns the test need when NeedDao.getNeedById()
     * returns null.
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedExists() throws IOException {
        //set up test data
        Need testNeed = new Need(1, "Canned Soup", 2.50, 50, "food");
        
        //force mockDao.getNeedById() to return test need
        when(mockDao.getNeedById(testNeed.getId())).thenReturn(testNeed);

        //check if method returns deleted need
        Need deletedNeed = service.deleteNeed(testNeed.getId());
        assertEquals(null, deletedNeed);
    }
}