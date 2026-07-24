package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.HelperAccount;
import com.ufund.api.ufundapi.model.ManagerAccount;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Proposal;
import com.ufund.api.ufundapi.service.AccountService;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.ProposalService;
import org.mockito.quality.Strictness;

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

        // "manager" is a stand-in for the X-Username header on requests that should be authorized
        when(accountService.getAccount("manager")).thenReturn(new ManagerAccount("manager", "pw"));
        // "helper" is a stand-in for a logged-in but non-manager caller, used by the forbidden tests
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
        when(needService.findNeeds("Corn")).thenReturn(List.of());
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
    public void testApprovalDuplicateNeed() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending");
        Need existingNeed = new Need(1, "Corn", 10.97, 100, "food");

        when(proposalService.getProposalById(3)).thenReturn(proposal);
        when(needService.findNeeds("Corn")).thenReturn(List.of(existingNeed));

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    public void testUpdateProposal() throws Exception {
        Proposal existing = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending");
        Proposal updated = new Proposal(3, "Corn Meal", 12.00, 150, "food", "moss", "moss inc", new HashMap<>(), "pending");

        when(proposalService.getProposalById(3)).thenReturn(existing);
        when(proposalService.updateProposal(updated)).thenReturn(updated);

        ResponseEntity<Proposal> response = proposalController.updateProposal("manager", 3, updated);

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

}
