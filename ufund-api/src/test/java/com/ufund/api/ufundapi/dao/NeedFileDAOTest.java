package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;

class NeedFileDAOTest {

    private NeedFileDAO needFileDAO;
    private Need[] testNeeds;
    private ObjectMapper mockObjectMapper;
    private Map<Integer, Need> mockNeeds;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = sampleNeeds();
        mockNeeds = mock(Map.class);

        when(mockObjectMapper
            .readValue(new File("testfile.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("testfile.txt",mockObjectMapper);
        
        
    }

    private Need[] sampleNeeds() {
        return new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, "food"),
            new Need(2, "Rice", 1.99, 100, "food"),
            new Need(3, "Blankets", 15.00, 25, "clothing")
        };
    }

    @Test
    void testGetAllNeedsReturnsAll() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.getAllNeeds();

        assertEquals(needs.length, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Canned Soup", result.get(0).getName());
        assertEquals(50, result.get(0).getQuantity());
        assertEquals("food", result.get(0).getType());
    }

    @Test
    void testGetAllNeedsReturnsNeedsOrderedById() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] unordered = {
            new Need(3, "Blankets", 15.00, 25, "clothing"),
            new Need(1, "Canned Soup", 2.50, 50, "food"),
            new Need(2, "Rice", 1.99, 100, "food")
        };
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(unordered);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.getAllNeeds();

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
    }

    @Test
    void testGetAllNeedsWhenEmpty() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(new Need[0]);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.getAllNeeds();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /************* deleteNeed() unit tests, implemented by Harikleia Sparakis*************/

    /**
     * Tests if NeedDao.deleteNeed() returns false when the map of needs does not contain the 
     * specified id
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedFalse() throws IOException {
        //setting up test data
        int needTestId = 1;

        //force 
        when(mockNeeds.containsKey(needTestId)).thenReturn(false);
        
        //test
        boolean result = needFileDAO.deleteNeed(needTestId);
        assertEquals(false, result);
    }

    /**
     * Tests if NeedDao.deleteNeed() returns true when the map of needs contains the 
     * specified id
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedExists() throws IOException {
        //setting up test data
        int needTestId = 1;

        //force 
        when(mockNeeds.containsKey(needTestId)).thenReturn(true);

        //test
        boolean result = needFileDAO.deleteNeed(needTestId);
        assertEquals(true, result);
        
    }
}