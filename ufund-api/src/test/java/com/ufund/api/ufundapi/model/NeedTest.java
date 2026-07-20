package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

@Tag("Model-Tier")
class NeedTest {
 @Test
    public void testConstructor() {
        // Setup
        int expectedId = 1;
        String expectedName = "Winter Coats";
        double expectedCost = 50.0;
        int expectedQuantity = 10;
        String expectedType = "clothing";

        // Invoke
        Need need = new Need(expectedId, expectedName, expectedCost, expectedQuantity, expectedType);

        // Analyze
        assertEquals(expectedId, need.getId());
        assertEquals(expectedName, need.getName());
        assertEquals(expectedCost, need.getCost());
        assertEquals(expectedQuantity, need.getQuantity());
        assertEquals(expectedType, need.getType());
    }

  @Test
    public void testSetId() {
        // Setup
        Need need = new Need(1, "Winter Coats", 50.0, 10, "clothing");
        int  newId = 23;

        // Invoke
        need.setId(newId);

        // Analyze
        assertEquals(newId, need.getId());
    }
  @Test
    public void testSetName() {
        // Setup
        Need need = new Need(1, "Winter Coats", 50.0, 10, "clothing");
        String newName = "Summer Hats";

        // Invoke
        need.setName(newName);

        // Analyze
        assertEquals(newName, need.getName());
    }
   @Test
    public void testSetCost() {
        // Setup
        Need need = new Need(1, "Winter Coats", 50.0, 10, "clothing");
        double newCost = 70.0;

        // Invoke
        need.setCost(newCost);

        // Analyze
        assertEquals(newCost, need.getCost());
    }
 @Test
    public void testSetQuantity() {
        // Setup
        Need need = new Need(1, "Winter Coats", 50.0, 10, "clothing");
	int newQuantity = 87;
        // Invoke
        need.setQuantity(newQuantity);

        // Analyze
        assertEquals(newQuantity, need.getQuantity());
    }
 @Test
    public void testSetType() {
        // Setup
        Need need = new Need(1, "Winter Coats", 50.0, 10, "clothing");
	String newType = "Coats";
        // Invoke
        need.setType(newType);

        // Analyze
        assertEquals(newType, need.getType());
    }
}

