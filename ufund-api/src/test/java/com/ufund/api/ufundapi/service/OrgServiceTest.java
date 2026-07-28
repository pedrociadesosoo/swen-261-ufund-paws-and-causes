package com.ufund.api.ufundapi.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.mockito.Mockito.mock;

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
}