package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    private Need[] sampleNeeds() {
        return new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION)
        };
    }

    @Test
    void testCreateNeedReturnsNeed() throws IOException {
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = sampleNeeds();
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        Need n = dao.createNeed(new Need(3, "Coat", 15.00, 25, NeedType.ITEM_DONATION));
        Need expected = new Need(4, "Coat", 15.00, 25, NeedType.ITEM_DONATION);

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

        Need n = dao.createNeed(new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION));
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
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION)
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
        Need expected = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION);
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
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };
        

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with item are returned
        List<Need> result = dao.findNeeds(NeedType.ITEM_DONATION);
        assertEquals(4, result.size());
        for(Need need : result){
            assertEquals(NeedType.ITEM_DONATION, need.getType());
        }
    }

    @Test
    void testFindNeedsTypeMonetary() throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with monetary are returned
        List<Need> result = dao.findNeeds(NeedType.MONETARY);
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
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with volunteering are returned
        List<Need> result = dao.findNeeds(NeedType.VOLUNTEERING);
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
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with volunteering are returned
        List<Need> result = dao.findNeeds(NeedType.VOLUNTEERING);
        assertEquals(0, result.size());
    }

    @Test
    void testFindNeedsBothItem()  throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with contained string and specificed type are returned
        List<Need> result = dao.findNeeds("a", NeedType.ITEM_DONATION);
        assertEquals(2, result.size());
    }

    @Test
    void testFindNeedsBothMonetary()  throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with contained string and specificed type are returned
        List<Need> result = dao.findNeeds("a", NeedType.MONETARY);
        assertEquals(1, result.size());
    }

    @Test
    void testFindNeedsBothVolunteering()  throws IOException {
        //set up test data
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        Need[] needs = new Need[] {
            new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION),
            new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION),
            new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION),
            new Need(4, "Monetary Test", 10, 10, NeedType.MONETARY),
            new Need(5, "Volunteering Test", 10, 10, NeedType.VOLUNTEERING),
            new Need(6, "Item Test", 1.99, 100, NeedType.ITEM_DONATION)
        };

        //force mockmapper to return needs
        when(mockMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);
        NeedFileDAO dao = new NeedFileDAO("data/needs.json", mockMapper);

        //test that only needs with contained string and specificed type are returned
        List<Need> result = dao.findNeeds("a", NeedType.VOLUNTEERING);
        assertEquals(0, result.size());
    }  

}
