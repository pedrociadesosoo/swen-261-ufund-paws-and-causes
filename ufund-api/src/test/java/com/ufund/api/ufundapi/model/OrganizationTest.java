package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
class OrganizationTest {

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

    @Test
    public void testAddNeed(){
        Organization org = sampleOrganization();
        Organization before = sampleOrganization();
        Need newNeed = new Need(4, "Shoes", 12.00, 20, NeedType.ITEM_DONATION, "test");
        org.addNeed(newNeed);
        assertEquals(before.getNeeds().size()+1, org.getNeeds().size());
        assertTrue(org.getNeeds().containsValue(newNeed));
        assertTrue(org.getNeeds().containsKey(newNeed.getId()));
        assertEquals(
            newNeed, 
            org.getNeeds().get(newNeed.getId()));
        assertEquals(
            newNeed, 
            org.getNeeds().get(4));
        assertEquals(
            before.getNeeds().get(1).getId(), 
            org.getNeeds().get(1).getId());
        assertEquals(
            before.getNeeds().get(2).getId(), 
            org.getNeeds().get(2).getId());
            assertEquals(
            before.getNeeds().get(3).getId(), 
            org.getNeeds().get(3).getId());
    }

    @Test
    public void testSetDescription(){
        Organization testOrg = sampleOrganization();
        testOrg.setDescription("new description");
        assertEquals("new description", testOrg.getDescription());

    }
}