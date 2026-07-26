package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.HelperAccount;
import com.ufund.api.ufundapi.model.ManagerAccount;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.model.Proposal;
import com.ufund.api.ufundapi.service.AccountService;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.ProposalService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProposalControllerTest {

    private ProposalController proposalController;
    private ProposalService proposalService;
    private NeedService needService;
    private AccountService accountService;

    @BeforeEach
    public void setupNeedController() throws IOException {
        proposalService = mock(ProposalService.class);
        needService = mock(NeedService.class);
        accountService = mock(AccountService.class);
        proposalController = new ProposalController(proposalService, needService, accountService);

        when(accountService.getAccount("manager")).thenReturn(new ManagerAccount("manager", "pw"));
        
        when(accountService.getAccount("helper")).thenReturn(new HelperAccount("helper", "pw", new ArrayList<>()));
    }

    @Test
    public void testDeleteProposal() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending" );
        when(proposalService.deleteProposal(3)).thenReturn(proposal);
        ResponseEntity<Proposal> response = proposalController.deleteProposal("manager", 3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    public void testDeleteProposalForbiddenForNonManager() throws Exception {
        ResponseEntity<Proposal> response = proposalController.deleteProposal("helper", 3);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).deleteProposal(any(int.class));
    }


    @Test
    public void testApproval() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending");

        when(proposalService.getProposalById(3)).thenReturn(proposal);
        when(needService.findNeeds("Corn", null)).thenReturn(List.of());
        when(proposalService.approveProposal(3)).thenReturn(proposal);

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    public void testApprovalProposalForbiddenForNonManager() throws Exception {
        ResponseEntity<?> response = proposalController.ApprovalProposal("helper", 3);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).approveProposal(anyInt());
    }


    @Test
    public void testApprovalSucceedsEvenWhenMatchingNeedAlreadyExists() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending");
        Need existingNeed = new Need(1, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "test");

        when(proposalService.getProposalById(3)).thenReturn(proposal);
        when(needService.findNeeds("Corn", null)).thenReturn(List.of(existingNeed));
        when(proposalService.approveProposal(3)).thenReturn(proposal);

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testUpdateProposal() throws Exception {
        Proposal existing = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending");
        Proposal updated = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>(), "pending");

        when(proposalService.getProposalById(3)).thenReturn(existing);
        when(proposalService.updateProposal(updated)).thenReturn(updated);

        ResponseEntity<?> response = proposalController.updateProposal("manager", 3, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }


    @Test
    public void testUpdateProposalForbiddenForNonManager() throws Exception {
        Proposal updated = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>(), "pending");
        ResponseEntity<?> response = proposalController.updateProposal("helper", 3, updated);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).updateProposal(any());
    }


    @Test
    public void testUpdateproposalDuplicateNeed() throws Exception {
        Proposal existing = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>(), "pending");
        Proposal updated = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>(), "pending");

        when (proposalService.getProposalById(3)).thenReturn(existing);
        when(proposalService.updateProposal(any(Proposal.class))).
            thenThrow(new IllegalArgumentException("Proposal status can't be updated, need already exist"));

        ResponseEntity<?> response = proposalController.updateProposal("manager", 3, updated);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRejectProposal() throws Exception {
        Proposal rejected = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>(), "pending");
        when(proposalService.rejectProposal(3)).thenReturn(rejected);

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rejected, response.getBody());
    }

    @Test
    public void testRejectProposalForbiddenForNonManager() throws Exception {
        ResponseEntity<?> response = proposalController.rejectProposal("helper", 3);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).rejectProposal(anyInt());
    }

    @Test
    public void testRejectproposalDuplicateNeed() throws Exception {

        when(proposalService.rejectProposal(3)).
            thenThrow(new IllegalArgumentException("Proposal status can't be updated, need already exist"));

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 3);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test 
    public void testRejectProposalAlreadyRejected() throws Exception {
        when(proposalService.rejectProposal(3)).thenThrow(new IllegalArgumentException("Proposal is already rejected"));

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 3);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Proposal is already rejected", response.getBody());
    }
}
