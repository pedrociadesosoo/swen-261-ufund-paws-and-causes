package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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

@Tag("Controller-tier")
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
        ResponseEntity<?> response = proposalController.deleteProposal("manager", 3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    public void testDeleteProposalForbiddenForNonManager() throws Exception {
        ResponseEntity<?> response = proposalController.deleteProposal("helper", 3);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).deleteProposal(any(int.class));
    }


    @Test
    public void testApproval() throws Exception {
        Proposal proposal = new Proposal(3, "Corn", 10.97, 100, "food", "moss", "moss inc", new HashMap<>(), "pending");

        when(proposalService.getProposalById(3)).thenReturn(proposal);
        when(needService.findNeeds("Corn", null, null)).thenReturn(List.of());
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
        when(needService.findNeeds("Corn", null, null)).thenReturn(List.of(existingNeed));
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

    @Test
    public void testDeleteProposalAlreadyDecided() throws Exception {
        when(proposalService.deleteProposal(3)).thenThrow(new IllegalStateException("Proposal has already been approved; only pending proposals can be deleted."));

        ResponseEntity<?> response = proposalController.deleteProposal("manager", 3);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Proposal has already been approved; only pending proposals can be deleted.", response.getBody());
    }

    /** Builds a pending proposal for the tests below. */
    private Proposal sampleProposal() {
        return new Proposal(3, "Corn", 10.97, 100, "ITEM_DONATION", "moss", "moss inc", new HashMap<>(), "pending");
    }

    // ---------- getProposal ----------

    @Test
    public void testGetProposalFound() throws Exception {
        Proposal proposal = sampleProposal();
        when(proposalService.getProposalById(3)).thenReturn(proposal);

        ResponseEntity<Proposal> response = proposalController.getProposal(3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    public void testGetProposalNotFound() throws Exception {
        when(proposalService.getProposalById(99)).thenReturn(null);

        ResponseEntity<Proposal> response = proposalController.getProposal(99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ---------- getProposals ----------

    /** With no search term the controller asks for the whole list. */
    @Test
    public void testGetProposalsNoSearchTerm() throws Exception {
        when(proposalService.getAllProposals()).thenReturn(List.of(sampleProposal(), sampleProposal()));

        ResponseEntity<Proposal[]> response = proposalController.getProposals(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        verify(proposalService).getAllProposals();
        verify(proposalService, never()).findProposals(any());
    }

    @Test
    public void testGetProposalsWithSearchTerm() throws Exception {
        when(proposalService.findProposals("Corn")).thenReturn(List.of(sampleProposal()));

        ResponseEntity<Proposal[]> response = proposalController.getProposals("Corn");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
        verify(proposalService).findProposals("Corn");
    }

    /** A blank search term is treated the same as no search term. */
    @Test
    public void testGetProposalsBlankSearchTerm() throws Exception {
        when(proposalService.getAllProposals()).thenReturn(List.of(sampleProposal()));

        ResponseEntity<Proposal[]> response = proposalController.getProposals("   ");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(proposalService).getAllProposals();
        verify(proposalService, never()).findProposals(any());
    }

    // ---------- createProposal ----------

    @Test
    public void testCreateProposal() throws Exception {
        Proposal proposal = sampleProposal();
        when(proposalService.createProposal(proposal)).thenReturn(proposal);

        ResponseEntity<Proposal> response = proposalController.createProposal(proposal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    /** The service returns null when the user already has a pending proposal. */
    @Test
    public void testCreateProposalConflict() throws Exception {
        Proposal proposal = sampleProposal();
        when(proposalService.createProposal(proposal)).thenReturn(null);

        ResponseEntity<Proposal> response = proposalController.createProposal(proposal);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateProposalIOException() throws Exception {
        Proposal proposal = sampleProposal();
        when(proposalService.createProposal(proposal)).thenThrow(new IOException("disk full"));

        ResponseEntity<Proposal> response = proposalController.createProposal(proposal);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ---------- voteUpdate ----------

    @Test
    public void testVoteUpdate() throws Exception {
        Proposal proposal = sampleProposal();
        when(proposalService.voteUpdate(3, "helper", 1)).thenReturn(proposal);

        ResponseEntity<?> response = proposalController.voteUpdate(3, new VoteRequest("helper", 1));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(proposal, response.getBody());
    }

    @Test
    public void testVoteUpdateProposalNotFound() throws Exception {
        when(proposalService.voteUpdate(99, "helper", 1)).thenReturn(null);

        ResponseEntity<?> response = proposalController.voteUpdate(99, new VoteRequest("helper", 1));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /** Voting on a decided proposal is refused by the service. */
    @Test
    public void testVoteUpdateOnDecidedProposal() throws Exception {
        when(proposalService.voteUpdate(3, "helper", 1))
            .thenThrow(new IllegalStateException("Proposal has already been approved; voting is only allowed while pending."));

        ResponseEntity<?> response = proposalController.voteUpdate(3, new VoteRequest("helper", 1));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Proposal has already been approved; voting is only allowed while pending.", response.getBody());
    }

    @Test
    public void testVoteUpdateIOException() throws Exception {
        when(proposalService.voteUpdate(3, "helper", 1)).thenThrow(new IOException("disk full"));

        ResponseEntity<?> response = proposalController.voteUpdate(3, new VoteRequest("helper", 1));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ---------- missing branches on the existing endpoints ----------

    /** A missing X-Username header is not a manager, so the request is refused. */
    @Test
    public void testDeleteProposalNullUsername() throws Exception {
        ResponseEntity<?> response = proposalController.deleteProposal(null, 3);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(proposalService, never()).deleteProposal(anyInt());
    }

    /** A blank X-Username header is refused the same way. */
    @Test
    public void testDeleteProposalBlankUsername() throws Exception {
        ResponseEntity<?> response = proposalController.deleteProposal("   ", 3);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testDeleteProposalNotFound() throws Exception {
        when(proposalService.deleteProposal(99)).thenReturn(null);

        ResponseEntity<?> response = proposalController.deleteProposal("manager", 99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProposalIOException() throws Exception {
        when(proposalService.deleteProposal(3)).thenThrow(new IOException("disk full"));

        ResponseEntity<?> response = proposalController.deleteProposal("manager", 3);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testApprovalProposalNotFound() throws Exception {
        when(proposalService.getProposalById(99)).thenReturn(null);

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(proposalService, never()).approveProposal(anyInt());
    }

    /** An unrecognized need type is reported as a bad request. */
    @Test
    public void testApprovalProposalInvalidType() throws Exception {
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.approveProposal(3))
            .thenThrow(new IllegalArgumentException("Proposal has an invalid type"));

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Proposal has an invalid type", response.getBody());
    }

    @Test
    public void testApprovalProposalAlreadyDecided() throws Exception {
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.approveProposal(3))
            .thenThrow(new IllegalStateException("Proposal has already been approved; only pending proposals can be approved."));

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testApprovalProposalIOException() throws Exception {
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.approveProposal(3)).thenThrow(new IOException("disk full"));

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * If the proposal disappears between the lookup and the approve, the
     * caller should get a plain 404 rather than a server error.
     */
    @Test
    public void testApprovalProposalVanishesMidRequest() throws Exception {
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.approveProposal(3)).thenReturn(null);

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testApprovalProposalUnexpectedError() throws Exception {
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.approveProposal(3)).thenThrow(new RuntimeException("boom"));

        ResponseEntity<?> response = proposalController.ApprovalProposal("manager", 3);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateProposalNotFound() throws Exception {
        when(proposalService.getProposalById(99)).thenReturn(null);

        ResponseEntity<?> response = proposalController.updateProposal("manager", 99, sampleProposal());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(proposalService, never()).updateProposal(any(Proposal.class));
    }

    /**
     * Editing a decided proposal is refused by the service, and that should
     * reach the caller as a conflict rather than a server error.
     */
    @Test
    public void testUpdateProposalAlreadyDecided() throws Exception {
        Proposal updated = sampleProposal();
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.updateProposal(updated))
            .thenThrow(new IllegalStateException("Proposal has already been approved; only pending proposals can be edited."));

        ResponseEntity<?> response = proposalController.updateProposal("manager", 3, updated);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Proposal has already been approved; only pending proposals can be edited.", response.getBody());
    }

    @Test
    public void testUpdateProposalVanishesMidRequest() throws Exception {
        Proposal updated = sampleProposal();
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.updateProposal(updated)).thenReturn(null);

        ResponseEntity<?> response = proposalController.updateProposal("manager", 3, updated);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateProposalIOException() throws Exception {
        Proposal updated = sampleProposal();
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.updateProposal(updated)).thenThrow(new IOException("disk full"));

        ResponseEntity<?> response = proposalController.updateProposal("manager", 3, updated);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateProposalUnexpectedError() throws Exception {
        Proposal updated = sampleProposal();
        when(proposalService.getProposalById(3)).thenReturn(sampleProposal());
        when(proposalService.updateProposal(updated)).thenThrow(new RuntimeException("boom"));

        ResponseEntity<?> response = proposalController.updateProposal("manager", 3, updated);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRejectProposalNotFound() throws Exception {
        when(proposalService.rejectProposal(99)).thenReturn(null);

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 99);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRejectProposalAlreadyDecided() throws Exception {
        when(proposalService.rejectProposal(3))
            .thenThrow(new IllegalStateException("Proposal has already been approved; only pending proposals can be rejected."));

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 3);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testRejectProposalIOException() throws Exception {
        when(proposalService.rejectProposal(3)).thenThrow(new IOException("disk full"));

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 3);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRejectProposalUnexpectedError() throws Exception {
        when(proposalService.rejectProposal(3)).thenThrow(new RuntimeException("boom"));

        ResponseEntity<?> response = proposalController.rejectProposal("manager", 3);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
