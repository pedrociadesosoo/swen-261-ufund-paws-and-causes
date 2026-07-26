package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
class NeedTest {
 @Test
    public void testConstructor() {
        int expectedId = 1;
        String expectedName = "Winter Coats";
        double expectedCost = 50.0;
        int expectedQuantity = 10;
        NeedType expectedType = NeedType.ITEM_DONATION;
        String expectedOrg = "testOrg";

        Need need = new Need(expectedId, expectedName, expectedCost, expectedQuantity, expectedType, expectedOrg);

        assertEquals(expectedId, need.getId());
        assertEquals(expectedName, need.getName());
        assertEquals(expectedCost, need.getCost());
        assertEquals(expectedQuantity, need.getQuantity());
        assertEquals(expectedType, need.getType());
        assertEquals(expectedOrg, need.getOrganization());
    }

  @Test
    public void testSetId() {
        Need need = new Need(1, "Winter Coats", 50.0, 10, NeedType.ITEM_DONATION, "test");
        int  newId = 23;

        need.setId(newId);
        assertEquals(newId, need.getId());
    }

  @Test
    public void testSetName() {
        Need need = new Need(1, "Winter Coats", 50.0, 10, NeedType.ITEM_DONATION, "test");
        String newName = "Summer Hats";

        need.setName(newName);
        assertEquals(newName, need.getName());
    }

   @Test
    public void testSetCost() {
        Need need = new Need(1, "Winter Coats", 50.0, 10, NeedType.ITEM_DONATION, "test");
        double newCost = 70.0;

        need.setCost(newCost);
        assertEquals(newCost, need.getCost());
    }

 @Test
    public void testSetQuantity() {
        Need need = new Need(1, "Winter Coats", 50.0, 10, NeedType.ITEM_DONATION, "test");
	int newQuantity = 87;

        need.setQuantity(newQuantity);
        assertEquals(newQuantity, need.getQuantity());
    }

 @Test
    public void testSetType() {
        Need need = new Need(1, "Winter Coats", 50.0, 10, NeedType.ITEM_DONATION, "test");
	NeedType newType = NeedType.MONETARY;

        need.setType(newType);
        assertEquals(newType, need.getType());
    }
}

