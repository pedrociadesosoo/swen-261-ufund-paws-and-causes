package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
class ProposalTest {
    private Proposal testProposal;
    Map<String,Integer> testVotes = new HashMap<String,Integer>();

    @BeforeEach
    public void setupProposalTest(){
        
        testVotes.put("p1", 3);
        testVotes.put("p2", -3);
        testVotes.put("p0", 0);

        testProposal = new Proposal(
            1, 
            "test", 
            1.30, 
            2, 
            "type", 
            "user", 
            "org", 
            testVotes, 
            "status");
    }

    @Test
    public void testGetId(){
        int result  = testProposal.getId();
        assertEquals(result, 1);
    }

    @Test
    public void testGetName(){
        String result  = testProposal.getName();
        assertEquals(result, "test");
    }

    @Test
    public void testGetCost(){
        double result  = testProposal.getCost();
        assertEquals(result, 1.30);
    }

    @Test
    public void testGetQuantity(){
        double result  = testProposal.getQuantity();
        assertEquals(result, 2);
    }

    @Test
    public void testGetType(){
        String result  = testProposal.getType();
        assertEquals(result, "type");
    }

    @Test
    public void testGetUserName(){
        String result  = testProposal.getUsername();
        assertEquals(result, "user");
    }

    @Test
    public void testGetOrg(){
        String result  = testProposal.getOrganization();
        assertEquals(result, "org");
    }

    @Test
    public void testGetStatus(){
        String result  = testProposal.getStatus();
        assertEquals(result, "status");
    }
    @Test
    public void testGetVotes(){
        Map<String,Integer> result  = testProposal.getAllVotes();
        assertEquals(result, testVotes);
    }

    // ---------- setters ----------

    @Test
    public void testSetId(){
        testProposal.setId(42);
        assertEquals(42, testProposal.getId());
    }

    @Test
    public void testSetName(){
        testProposal.setName("new name");
        assertEquals("new name", testProposal.getName());
    }

    @Test
    public void testSetCost(){
        testProposal.setCost(99.99);
        assertEquals(99.99, testProposal.getCost());
    }

    @Test
    public void testSetQuantity(){
        testProposal.setQuantity(7);
        assertEquals(7, testProposal.getQuantity());
    }

    @Test
    public void testSetType(){
        testProposal.setType("MONETARY");
        assertEquals("MONETARY", testProposal.getType());
    }

    @Test
    public void testSetOrganization(){
        testProposal.setOrganization("new org");
        assertEquals("new org", testProposal.getOrganization());
    }

    @Test
    public void testSetStatus(){
        testProposal.setStatus("approved");
        assertEquals("approved", testProposal.getStatus());
    }

    @Test
    public void testSetAllVotes(){
        Map<String,Integer> newVotes = new HashMap<String,Integer>();
        newVotes.put("p3", 1);

        testProposal.setAllVotes(newVotes);

        assertEquals(newVotes, testProposal.getAllVotes());
    }

    // ---------- dates ----------

    /** A proposal stamps itself with a creation date when it's built. */
    @Test
    public void testCreationDateDefaultsToNow(){
        assertNotNull(testProposal.getCreationDate());
    }

    @Test
    public void testSetCreationDate(){
        testProposal.setCreationDate("2026-07-28T00:00:00Z");
        assertEquals("2026-07-28T00:00:00Z", testProposal.getCreationDate());
    }

    /** lastEdited stays null until something sets it. */
    @Test
    public void testLastEditedStartsNull(){
        assertNull(testProposal.getLastEdited());
    }

    @Test
    public void testSetLastEdited(){
        testProposal.setLastEdited("2026-07-28T00:00:00Z");
        assertEquals("2026-07-28T00:00:00Z", testProposal.getLastEdited());
    }

    // ---------- individual vote lookup ----------

    @Test
    public void testGetUserVoteStatus(){
        assertEquals(3, testProposal.getUserVoteStatus("p1"));
        assertEquals(-3, testProposal.getUserVoteStatus("p2"));
    }

    /** A user who hasn't voted has no vote status. */
    @Test
    public void testGetUserVoteStatusUnknownUser(){
        assertNull(testProposal.getUserVoteStatus("nobody"));
    }
}

