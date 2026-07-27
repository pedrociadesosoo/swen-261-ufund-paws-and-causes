package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ufund.api.ufundapi.dao.OrganizationFileDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.model.Organization;

@Tag("Service-tier")
class OrgServiceTest {

    private OrganizationServiceImpl service;
    private OrganizationFileDAO mockDao;

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
    void setup(){
        mockDao = mock(OrganizationFileDAO.class);
        service = new OrganizationServiceImpl(mockDao);
    }

    @Test
    public void testAddNeedtoOrg() throws IOException{
        
        Organization o = sampleOrganization();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");

        Map<Integer, Need> saveNeeds = o.getNeeds();

        boolean added = service.addNeed(service.getOrganization(o.getName()), newNeed);
        //assertTrue(added);
        assertEquals(4, o.getNeeds().size());
        //assertEquals(sampleOrganization().getNeeds().size()+1, mockDao.getOrganization("General Humanities").getNeeds().size());
        //assertTrue(mockDao.getOrganization("General Humanities").getNeeds().containsValue(newNeed));
        //assertTrue(mockDao.getOrganization("General Humanities").getNeeds().containsKey(newNeed.getId()));
        assertEquals(
            newNeed, 
            mockDao.getOrganization("General Humanities").getNeeds().get(newNeed.getId()));
        assertEquals(
            newNeed, 
            mockDao.getOrganization("General Humanities").getNeeds().get(4));
        assertEquals(
            saveNeeds.get(1), 
            mockDao.getOrganization("General Humanities").getNeeds().get(1));
        assertEquals(
            saveNeeds.get(2), 
            mockDao.getOrganization("General Humanities").getNeeds().get(2));
            assertEquals(
            saveNeeds.get(3), 
            mockDao.getOrganization("General Humanities").getNeeds().get(3));
        verify(mockDao, times(1)).addNeed(o, newNeed);
    }
}