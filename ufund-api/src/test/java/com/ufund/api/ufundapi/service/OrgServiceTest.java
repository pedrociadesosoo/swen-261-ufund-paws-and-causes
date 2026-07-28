package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public  void setupOrgServiceTest(){
        mockDao = mock(OrganizationFileDAO.class);
        service = new OrganizationServiceImpl(mockDao);
    }

    @Test
    public void testGetOrganization() throws IOException{
        String testName = "test";
        service.getOrganization(testName);
        verify(mockDao, times(1)).getOrganization(testName);
    }

    @Test
    public void testGetOrganizationArray() throws IOException{
        service.getOrganizationArray();
        verify(mockDao, times(1)).getOrganizationArray();
    }

    @Test
    public void testCreateOrganization() throws IOException{
        Organization testOrg = sampleOrganization();
        service.createOrganization(testOrg);
        verify(mockDao, times(1)).createOrganization(testOrg);
    }

    @Test
    public void testUpadteOrganization() throws IOException{
        Organization testOrg = sampleOrganization();
        service.updateOrganization(testOrg, testOrg.getName());
        verify(mockDao, times(1)).updateOrganization(testOrg, testOrg.getName());
    }

    @Test
    public void testDeleteOrganization() throws IOException{
        Organization testOrg = sampleOrganization();
        service.deleteOrganization(testOrg.getName());
        verify(mockDao, times(1)).deleteOrganization(testOrg.getName());
    }

    @Test
    public void testAddNeed() throws IOException{
        Organization testOrg = sampleOrganization();
        Need testNeed = new Need(5, "test", 2,2,NeedType.ITEM_DONATION, testOrg.getName());
        service.addNeed(testOrg, testNeed);
        verify(mockDao, times(1)).addNeed(testOrg, testNeed);
    }

    @Test
    public void testDeleteNeed() throws IOException{
        Organization testOrg = sampleOrganization();
        Need testNeed = testOrg.getNeeds().get(1);
        service.deleteNeed(testOrg, testNeed);
        verify(mockDao, times(1)).deleteNeed(testOrg, testNeed);
    }
}