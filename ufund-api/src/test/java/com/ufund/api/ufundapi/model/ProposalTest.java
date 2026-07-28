package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

  
}

