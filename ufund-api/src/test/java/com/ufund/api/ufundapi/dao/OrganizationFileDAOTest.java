package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.model.Organization;

@Tag("Persistence-tier")
public class OrganizationFileDAOTest {

    private ObjectMapper mockMapper;
    private OrganizationFileDAO oDao;
    private Map<Integer, Organization> mockOrgs;

    private Need[] sampleNeeds() {
        return new Need[] {
                new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test"),
                new Need(2, "Rice", 1.99, 100, NeedType.ITEM_DONATION, "test"),
                new Need(3, "Blankets", 15.00, 25, NeedType.ITEM_DONATION, "test")
        };
    }

    private Organization sampleOrganization(){
        Need[] needs = sampleNeeds();
        Map<Integer, Need> needMap = new HashMap<>();

        for (Need need : needs){
            needMap.put(need.getId(), need);
        }

        return new Organization("General Humanities", 
                                "We're a nonprofit general charity working for a variety of causes",
                                needMap);
    }

    @BeforeEach
    public void setupDAO() throws IOException {
        mockMapper = mock(ObjectMapper.class);
        when(mockMapper.readValue(any(File.class), eq(Organization[].class)))
            .thenReturn(new Organization[] { sampleOrganization() });
        oDao = new OrganizationFileDAO("data/organizations.json", mockMapper);
        mockOrgs = mock(Map.class);
    }

    @Test
    void testGetOrgReturnsOrg() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        assertEquals("General Humanities", o.getName());
        assertEquals("We're a nonprofit general charity working for a variety of causes", o.getDescription());
        assertEquals(3, o.getNeeds().size());
    }

    @Test
    void testGetOrgMissing() throws IOException{
        Organization o = oDao.getOrganization("gleep glorp");
        assertNull(o);
    }

    @Test
    void testGetOrgArray() throws IOException{
        Organization[] o = oDao.getOrganizationArray();
        assertEquals(1, o.length);
        assertEquals("General Humanities", o[0].getName());
    }

    @Test
    void testCreateOrgReturnsOrg() throws IOException{
        Organization created = oDao.createOrganization(new Organization("Forest Fighters", "We fight forests", new HashMap<>()));

        assertEquals(created.getName(), "Forest Fighters");
        assertEquals(created.getDescription(), "We fight forests");
        assertTrue(created.getNeeds().isEmpty());
    }

    @Test
    void testCreateOrgFail() throws IOException{
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(Organization[].class));

        Organization created = oDao.createOrganization(new Organization("Forest Fighters", "We fight forests", new HashMap<>()));
        assertNull(created);
    }

    @Test
    void testDeleteOrgReturnsTrue() throws IOException{
        boolean deleted = oDao.deleteOrganization("General Humanities");

        assertEquals(true, deleted);
        assertNull(oDao.getOrganization("General Humanities"));
    }

    @Test
    void testDeleteOrgMissingReturnsFalse() throws IOException{
        boolean deleted = oDao.deleteOrganization("Forest Fighters");
        
        assertFalse(deleted);
        assertEquals(1, oDao.getOrganizationArray().length);
    }

    @Test
    void testAddNeed() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");

        Map<Integer, Need> saveNeeds = o.getNeeds();

        boolean added = oDao.addNeed(o, newNeed);
        assertTrue(added);
        assertEquals(4, oDao.getOrganization("General Humanities").getNeeds().size());
        assertEquals(sampleOrganization().getNeeds().size()+1, oDao.getOrganization("General Humanities").getNeeds().size());
        assertTrue(oDao.getOrganization("General Humanities").getNeeds().containsValue(newNeed));
        assertTrue(oDao.getOrganization("General Humanities").getNeeds().containsKey(newNeed.getId()));
        assertEquals(
            newNeed, 
            oDao.getOrganization("General Humanities").getNeeds().get(newNeed.getId()));
        assertEquals(
            newNeed, 
            oDao.getOrganization("General Humanities").getNeeds().get(4));
        assertEquals(
            saveNeeds.get(1), 
            oDao.getOrganization("General Humanities").getNeeds().get(1));
        assertEquals(
            saveNeeds.get(2), 
            oDao.getOrganization("General Humanities").getNeeds().get(2));
            assertEquals(
            saveNeeds.get(3), 
            oDao.getOrganization("General Humanities").getNeeds().get(3));
    }

    @Test
    void testAddNeedConflict() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");

        boolean added = oDao.addNeed(o, newNeed);
        assertFalse(added);
        assertEquals(3, oDao.getOrganization("General Humanities").getNeeds().size());
    }

    @Test
    void testAddNeedSaveFail() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(Organization[].class));

        boolean added = oDao.addNeed(o, newNeed);
        assertFalse(added);
    }

    @Test
    void testDeleteNeed() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need deletedNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");

        boolean deleted = oDao.deleteNeed(o, deletedNeed);
        assertTrue(deleted);
        assertEquals(2, oDao.getOrganization("General Humanities").getNeeds().size());
    }

    @Test
    void testDeleteNeedNotFound() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need deletedNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");

        boolean deleted = oDao.deleteNeed(o, deletedNeed);
        assertFalse(deleted);
        assertEquals(3, oDao.getOrganization("General Humanities").getNeeds().size());
    }

    @Test
    void testDeleteNeedHandleException() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need deletedNeed = new Need(1, "Canned Soup", 2.50, 50, NeedType.ITEM_DONATION, "test");
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(Organization[].class));

        boolean deleted = oDao.deleteNeed(o, deletedNeed);
        assertFalse(deleted);
    }

    @Test
    public void testUpdateOrgKey() throws IOException {
        //setting up test data
        Organization o = oDao.getOrganization("General Humanities");
        Organization testOrg = new Organization("test", "description", null);

        //force 
        when(mockOrgs.containsKey("General Humanities")).thenReturn(true);
        
        //test
        Organization result = oDao.updateOrganization(testOrg, o.getName());
        assertEquals(testOrg, result);
    }

    @Test
    void testUpdateOrgFail() throws IOException{
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(Organization[].class));

        Organization updated = oDao.updateOrganization(new Organization("Forest Fighters", "We fight forests", new HashMap<>()), "General Humanities");
        assertNull(updated);
    }

    @Test
    void testDeleteOrgFail() throws IOException{
        doThrow(new IOException("write failed")).when(mockMapper).writeValue(any(File.class), any(Organization[].class));

        boolean deleted = oDao.deleteOrganization("General Humanities");
        assertFalse(deleted);
    }

}
