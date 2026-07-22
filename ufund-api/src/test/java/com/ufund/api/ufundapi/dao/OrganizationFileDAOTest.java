package com.ufund.api.ufundapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Organization;
import com.ufund.api.ufundapi.model.FundingBasket;
import com.ufund.api.ufundapi.model.Need;

@Tag("Persistence-tier")
public class OrganizationFileDAOTest {

    private ObjectMapper mockMapper;
    private OrganizationFileDAO oDao;

    private Need[] sampleNeeds() {
        return new Need[] {
                new Need(1, "Canned Soup", 2.50, 50, "food"),
                new Need(2, "Rice", 1.99, 100, "food"),
                new Need(3, "Blankets", 15.00, 25, "clothing")
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
        Need newNeed = new Need(4, "Shoes", 12.00, 20, "clothing");

        boolean added = oDao.addNeed(o, newNeed);
        assertTrue(added);
        assertEquals(4, oDao.getOrganization("General Humanities").getNeeds().size());
    }

    @Test
    void testAddNeedConflict() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need newNeed = new Need(1, "Canned Soup", 2.50, 50, "food");

        boolean added = oDao.addNeed(o, newNeed);
        assertFalse(added);
        assertEquals(3, oDao.getOrganization("General Humanities").getNeeds().size());
    }

    @Test
    void testDeleteNeed() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need deletedNeed = new Need(1, "Canned Soup", 2.50, 50, "food");

        boolean deleted = oDao.deleteNeed(o, deletedNeed);
        assertTrue(deleted);
        assertEquals(2, oDao.getOrganization("General Humanities").getNeeds().size());
    }

    @Test
    void testDeleteNeedNotFound() throws IOException{
        Organization o = oDao.getOrganization("General Humanities");
        Need deletedNeed = new Need(4, "Shoes", 12.00, 20, "clothing");

        boolean deleted = oDao.deleteNeed(o, deletedNeed);
        assertFalse(deleted);
        assertEquals(3, oDao.getOrganization("General Humanities").getNeeds().size());
    }

}
