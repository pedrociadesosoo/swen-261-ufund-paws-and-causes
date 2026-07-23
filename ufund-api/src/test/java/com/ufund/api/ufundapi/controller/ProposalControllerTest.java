package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Proposal;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.ProposalService;

@ExtendWith(MockitoExtension.class)
public class ProposalControllerTest {

    @Mock
    private ProposalService proposalService;

    @Mock
    private NeedService needService;

    @InjectMocks
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
    public void testApproval() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>());

        when(proposalService.getProposalById(3)).thenReturn(proposal);
        when(needService.findNeeds("Corn")).thenReturn(List.of());
        when(proposalService.approveProposal(3)).thenReturn(proposal);

        ResponseEntity<?> response = proposalController.ApprovalProposal(3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }



    @Test
    public void testApprovalDuplicateNeed() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>());
        Need existingNeed = new Need(1, "Corn", 10.97, 100, "food");

        when(proposalService.getProposalById(3)).thenReturn(proposal);
        when(needService.findNeeds("Corn")).thenReturn(List.of(existingNeed));

        ResponseEntity<?> response = proposalController.ApprovalProposal(3);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    public void testUpdateProposal() throws Exception {
        Proposal existing = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>());
        Proposal updated = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>());

        when(proposalService.getProposalById(3)).thenReturn(existing);
        when(proposalService.updateProposal(updated)).thenReturn(updated);

        ResponseEntity<Proposal> response = proposalController.updateProposal(3, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }
}
