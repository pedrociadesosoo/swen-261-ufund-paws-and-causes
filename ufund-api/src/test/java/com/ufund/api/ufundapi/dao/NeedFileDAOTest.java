package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;

@Tag("Persistence-tier")
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
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test")
        };
    }

    @Test
    void testCreateNeedReturnsNeed() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        Need n = dao.createNeed(new Need(3, "Coat", 15.00, 25, NeedType.ITEM_DONATION, "test"));
        Need expected = new Need(4, "Coat", 15.00, 25, NeedType.ITEM_DONATION, "test");

        assertEquals(expected.getId(), n.getId());
        assertEquals(expected.getName(), n.getName());
        assertEquals(expected.getCost(), n.getCost());
        assertEquals(expected.getQuantity(), n.getQuantity());
        assertEquals(expected.getType(), n.getType());

    }

    @Test
    void testCreateNeedFail() throws IOException{
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        Need n = dao.createNeed(new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"));
        assertEquals(null, n);
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
        assertEquals(NeedType.ITEM_DONATION, result.get(0).getType());
    }

    @Test
    void testGetAllNeedsReturnsNeedsOrderedById() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] unordered = {
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test")
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

    @Test
    void testGetNeedByIdReturnsNeed() throws IOException{
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();

        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        Need result = dao.getNeedById(1);
        Need expected = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getId(), result.getId());

    }

    @Test
    void testGetNeedByIdFailure() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();

        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        Need result = dao.getNeedById(4);
        assertEquals(null, result);
    }


    // Pedrocia's unit tests - findNeeds

    /**
     *Verifies that findNeeds returns needs whose name contains the search term.
     */
    @Test
    void testFindNeeds() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.findNeeds("Canned");

        assertEquals(1, result.size());
        assertEquals("Canned Soup", result.get(0).getName());
    }

    /**
     * Verifies that findNeeds is case-insensitive.
     */
    @Test
    void testFindNeedsCaseInsensitive() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.findNeeds("canned");

        assertEquals(1, result.size());
        assertEquals("Canned Soup", result.get(0).getName());
    }

    /**
     * Verifies that findNeeds returns empty list when no needs match the search term
     */
    @Test
    void testFindNeedsNoMatch() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.findNeeds("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Verifies that findNeeds with null returns all needs
     */
    @Test
    void testFindNeedsNullReturnsAll() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);

        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);
        List<Need> result = dao.findNeeds((String) null);

        assertEquals(3, result.size());
    }

    @Test
    void testFindNeedsTypeItem() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };
        

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeedsWithType(NeedType.ITEM_DONATION);
        assertEquals(4, result.size());
        for(Need need : result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
        }
    }

    @Test
    void testFindNeedsTypeAll() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };
        
        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeeds("a", NeedType.ITEM_DONATION, "test");
        assertEquals(2, result.size());
        for(Need need : result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
            assertTrue(need.getName().contains("a"));
            assertEquals("test", need.getOrganization());
        }
    }

    @Test
    void testFindNeedsTypeNoType() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };
        
        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeeds("e", "test");
        for(Need need : result){
            assertTrue(need.getName().contains("e"));
            assertEquals("test", need.getOrganization());
        }
    }

    @Test
    void testFindNeedsNoName() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };
        
        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeeds(NeedType.ITEM_DONATION, "test");
        for(Need need : result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
            assertEquals("test", need.getOrganization());
        }
    }

    @Test
    void testFindNeedsNoOrg() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "e"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "e")
        };
        
        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeeds("a", NeedType.ITEM_DONATION);
        assertEquals(2, result.size());
        for(Need need : result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
            assertTrue(need.getName().contains("a"));
        }
    }

    @Test
    void testFindNeedsName() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "e"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "e")
        };
        
        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeedsWithOrg("a");
        for(Need need : result){
            assertTrue(need.getName().contains("a"));
        }
    }


    @Test
    void testFindNeedsTypeMonetary() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with monetary are returned
        List<Need> result = dao.findNeedsWithType(NeedType.MONETARY);
        assertEquals(1, result.size());
        for(Need need : result){
            assertEquals(NeedType.MONETARY, need.getType());
        }
    }

    @Test
    void testFindNeedsTypeVolunteering() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with volunteering are returned
        List<Need> result = dao.findNeedsWithType(NeedType.VOLUNTEERING);
        assertEquals(1, result.size());
        for(Need need : result){
            assertEquals(NeedType.VOLUNTEERING, need.getType());
        }
    }

    @Test
    void testFindNeedsTypeNoMatches() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with volunteering are returned
        List<Need> result = dao.findNeedsWithType(NeedType.VOLUNTEERING);
        assertEquals(0, result.size());
    }

    @Test
    void testFindNeedsBothItem()  throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with contained string and specificed type are returned
        List<Need> result = dao.findNeeds("a", NeedType.ITEM_DONATION);
        assertEquals(2, result.size());
        for(Need need : result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
            assertTrue(need.getName().contains("a"));
        }       
    }

    @Test
    void testFindNeedsBothMonetary()  throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with contained string and specificed type are returned
        List<Need> result = dao.findNeeds("a", NeedType.MONETARY);
        assertEquals(1, result.size());

        for(Need need : result){
            assertEquals(NeedType.MONETARY, need.getType());
            assertTrue(need.getName().contains("a"));
        }
    }

    @Test
    void testFindNeedsBothVolunteering()  throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test"),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY, "test"),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING, "test"),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION, "test")
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with contained string and specificed type are returned
        List<Need> result = dao.findNeeds("a", NeedType.VOLUNTEERING);
        assertEquals(0, result.size());
    }  

    /**
     * Tests if NeedDao.deleteNeed() returns false when the map of needs does not contain the 
     * specified id
     * 
     * @throws IOException
     */
    @Test
    public void testDeleteNeedFalse() throws IOException {
        //setting up test data
        int needTestId = 10;

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

    /**
     * Tests if NeedDao.updateNeed() returns false when the map of needs does not contain the 
     * specified id
     * 
     * @throws IOException
     */
    @Test
    public void testUpdateNeedNoKey() throws IOException {
        //setting up test data
        int needTestId = 10;
        Need testNeed =  new Need(needTestId, "test", 0, 0, NeedType.ITEM_DONATION, "test");

        //force 
        when(mockNeeds.containsKey(needTestId)).thenReturn(false);
        
        //test
        Need result = needFileDAO.updateNeed(testNeed);
        assertEquals(null, result);
    }

    /**
     * Tests if NeedDao.updateNeed() returns true when the map of needs contains the 
     * specified id
     * 
     * @throws IOException
     */
    @Test
    public void testUpdateNeedKey() throws IOException {
        //setting up test data
        int needTestId = 1;
        Need testNeed =  new Need(needTestId, "test", 0, 0, NeedType.ITEM_DONATION, "test");

        //force 
        when(mockNeeds.containsKey(needTestId)).thenReturn(true);

        //test
        Need result = needFileDAO.updateNeed(testNeed);
        assertEquals(testNeed, result);
    }

}
