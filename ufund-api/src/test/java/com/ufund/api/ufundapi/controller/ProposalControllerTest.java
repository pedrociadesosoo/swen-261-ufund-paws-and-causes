package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.assertj.core.api.ObjectEnumerableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Proposal;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.ProposalService;

import java.util.HashMap;
import java.util.Map;

public class ProposalControllerTest {
    private NeedController needController;
    private NeedService needService;
    private ProposalService proposalService;
    private ProposalController proposalController;


    @Test
    public void testDeleteProposal() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>() );
        when(proposalService.deleteProposal(3)).thenReturn(proposal);
        ResponseEntity<Proposal> response = proposalController.deleteProposal("manager", 3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    void testDeleteProposalForbiddenForNonManager() throws IOException {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>() );
        when(proposalService.deleteProposal(1)).thenReturn(proposal);
        ResponseEntity<Proposal> response = proposalController.deleteProposal("manager", 3);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).createProposal(any());
    }





}
