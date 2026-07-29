package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufund.api.ufundapi.dao.ProposalDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedType;
import com.ufund.api.ufundapi.model.Proposal;

@Tag("Service-tier")
class ProposalServiceTest {

    private ProposalServiceImpl service;
    private ProposalDAO mockDao;
    private NeedService mockNeedService;

    @BeforeEach
    void setup() {
        mockDao = mock(ProposalDAO.class);
        mockNeedService = mock(NeedService.class);
        service = new ProposalServiceImpl(mockDao, mockNeedService);
    }

    /**
     * Builds a pending proposal with a valid need type, which is the starting
     * point most of these tests need.
     */
    private Proposal pendingProposal() {
        return new Proposal(1, "Corn", 10.97, 100, "ITEM_DONATION",
            "helper", "moss inc", new HashMap<>(), "pending");
    }

    // ---------- pass-through methods ----------

    @Test
    void testGetAllProposalsReturnsAll() {
        List<Proposal> proposals = Arrays.asList(pendingProposal(), pendingProposal());
        when(mockDao.getAllProposals()).thenReturn(proposals);

        List<Proposal> result = service.getAllProposals();

        assertEquals(2, result.size());
        verify(mockDao).getAllProposals();
    }

    @Test
    void testGetAllProposalsWhenEmpty() {
        when(mockDao.getAllProposals()).thenReturn(Collections.emptyList());

        assertTrue(service.getAllProposals().isEmpty());
        verify(mockDao).getAllProposals();
    }

    @Test
    void testFindProposalsReturnsMatches() {
        when(mockDao.findProposals("Corn")).thenReturn(List.of(pendingProposal()));

        List<Proposal> result = service.findProposals("Corn");

        assertEquals(1, result.size());
        assertEquals("Corn", result.get(0).getName());
        verify(mockDao).findProposals("Corn");
    }

    @Test
    void testFindProposalsNoMatch() {
        when(mockDao.findProposals("xyz")).thenReturn(Collections.emptyList());

        assertTrue(service.findProposals("xyz").isEmpty());
        verify(mockDao).findProposals("xyz");
    }

    @Test
    void testGetProposalByIdFound() throws IOException {
        Proposal proposal = pendingProposal();
        when(mockDao.getProposalById(1)).thenReturn(proposal);

        assertEquals(proposal, service.getProposalById(1));
        verify(mockDao).getProposalById(1);
    }

    @Test
    void testGetProposalByIdNotFound() throws IOException {
        when(mockDao.getProposalById(99)).thenReturn(null);

        assertNull(service.getProposalById(99));
    }

    /**
     * Every new proposal should start out pending and get a creation date
     * stamped on it, whatever the caller sent.
     */
    @Test
    void testCreateProposalStampsStatusAndDate() throws IOException {
        Proposal incoming = new Proposal(0, "Corn", 10.97, 100, "ITEM_DONATION",
            "helper", "moss inc", new HashMap<>(), "approved");
        // Proposal fills in a creation date on its own, so start from a
        // sentinel value to prove the service really replaced it.
        incoming.setCreationDate("not-a-real-date");
        when(mockDao.createProposal(incoming)).thenReturn(incoming);

        Proposal result = service.createProposal(incoming);

        assertEquals("pending", result.getStatus());
        assertNotEquals("not-a-real-date", result.getCreationDate());
        verify(mockDao).createProposal(incoming);
    }

    /**
     * Editing a pending proposal keeps it pending so it goes back through review.
     */
    @Test
    void testUpdateProposalForcesPending() throws IOException {
        Proposal edited = new Proposal(1, "Corn Meal", 12.00, 150, "ITEM_DONATION",
            "helper", "moss inc", new HashMap<>(), "approved");
        when(mockDao.getProposalById(1)).thenReturn(pendingProposal());
        when(mockDao.updateProposal(edited)).thenReturn(edited);

        Proposal result = service.updateProposal(edited);

        assertEquals("pending", result.getStatus());
        verify(mockDao).updateProposal(edited);
    }

    @Test
    void testUpdateProposalNotFound() throws IOException {
        Proposal edited = new Proposal(99, "Ghost", 1.00, 1, "ITEM_DONATION",
            "helper", "moss inc", new HashMap<>(), "pending");
        when(mockDao.getProposalById(99)).thenReturn(null);

        assertNull(service.updateProposal(edited));
        verify(mockDao, never()).updateProposal(edited);
    }

    /**
     * An already-decided proposal can't be edited back into the pending queue.
     * The stored status is what counts, not whatever the request body claims.
     */
    @Test
    void testUpdateProposalAlreadyDecidedIsRefused() throws IOException {
        Proposal stored = pendingProposal();
        stored.setStatus("approved");
        Proposal edited = new Proposal(1, "Corn Meal", 12.00, 150, "ITEM_DONATION",
            "helper", "moss inc", new HashMap<>(), "pending");
        when(mockDao.getProposalById(1)).thenReturn(stored);

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> service.updateProposal(edited));

        assertTrue(e.getMessage().contains("already been approved"));
        verify(mockDao, never()).updateProposal(edited);
    }

    // ---------- deleteProposal ----------

    @Test
    void testDeleteProposalNotFound() throws IOException {
        when(mockDao.getProposalById(99)).thenReturn(null);

        assertNull(service.deleteProposal(99));
        verify(mockDao, never()).deleteProposal(99);
    }

    /**
     * Only pending proposals can be deleted; a decided one is refused.
     */
    @Test
    void testDeleteProposalAlreadyDecided() throws IOException {
        Proposal approved = pendingProposal();
        approved.setStatus("approved");
        when(mockDao.getProposalById(1)).thenReturn(approved);

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> service.deleteProposal(1));

        assertTrue(e.getMessage().contains("already been approved"));
        verify(mockDao, never()).deleteProposal(1);
    }

    @Test
    void testDeleteProposalSuccess() throws IOException {
        Proposal proposal = pendingProposal();
        when(mockDao.getProposalById(1)).thenReturn(proposal);
        when(mockDao.deleteProposal(1)).thenReturn(true);

        assertEquals(proposal, service.deleteProposal(1));
        verify(mockDao).deleteProposal(1);
    }

    /**
     * If the DAO reports the delete didn't happen, the service returns null.
     */
    @Test
    void testDeleteProposalDaoReportsFailure() throws IOException {
        when(mockDao.getProposalById(1)).thenReturn(pendingProposal());
        when(mockDao.deleteProposal(1)).thenReturn(false);

        assertNull(service.deleteProposal(1));
    }

    // ---------- approveProposal ----------

    @Test
    void testApproveProposalNotFound() throws IOException {
        when(mockDao.getProposalById(99)).thenReturn(null);

        assertNull(service.approveProposal(99));
        verify(mockNeedService, never()).createNeed(any(Need.class));
    }

    @Test
    void testApproveProposalAlreadyDecided() throws IOException {
        Proposal rejected = pendingProposal();
        rejected.setStatus("rejected");
        when(mockDao.getProposalById(1)).thenReturn(rejected);

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> service.approveProposal(1));

        assertTrue(e.getMessage().contains("already been rejected"));
    }

    /**
     * The proposal's type is a free-text string, so it may not match any
     * NeedType. Approving should explain that rather than blow up.
     */
    @Test
    void testApproveProposalInvalidType() throws IOException {
        Proposal proposal = pendingProposal();
        proposal.setType("food");
        when(mockDao.getProposalById(1)).thenReturn(proposal);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> service.approveProposal(1));

        assertTrue(e.getMessage().contains("invalid type"));
    }

    /**
     * A null type hits a NullPointerException inside valueOf, which is caught
     * and reported the same way as an unrecognized type.
     */
    @Test
    void testApproveProposalNullType() throws IOException {
        Proposal proposal = pendingProposal();
        proposal.setType(null);
        when(mockDao.getProposalById(1)).thenReturn(proposal);

        assertThrows(IllegalArgumentException.class, () -> service.approveProposal(1));
    }

    /**
     * Approving is blocked when a need by that name already exists. The check
     * ignores case, so "corn" collides with "Corn".
     */
    @Test
    void testApproveProposalDuplicateNeedName() throws IOException {
        when(mockDao.getProposalById(1)).thenReturn(pendingProposal());
        when(mockNeedService.getAllNeeds()).thenReturn(
            List.of(new Need(5, "corn", 1.0, 1, NeedType.ITEM_DONATION, "moss inc")));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> service.approveProposal(1));

        assertEquals("Need name already exist", e.getMessage());
        verify(mockNeedService, never()).createNeed(any(Need.class));
    }

    @Test
    void testApproveProposalCreateNeedFails() throws IOException {
        when(mockDao.getProposalById(1)).thenReturn(pendingProposal());
        when(mockNeedService.getAllNeeds()).thenReturn(Collections.emptyList());
        when(mockNeedService.createNeed(any(Need.class))).thenReturn(null);

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> service.approveProposal(1));

        assertEquals("creation failed", e.getMessage());
    }

    /**
     * The happy path: a need is created from the proposal's fields and the
     * proposal is marked approved.
     */
    @Test
    void testApproveProposalSuccess() throws IOException {
        Proposal proposal = pendingProposal();
        when(mockDao.getProposalById(1)).thenReturn(proposal);
        when(mockNeedService.getAllNeeds()).thenReturn(Collections.emptyList());
        when(mockNeedService.createNeed(any(Need.class)))
            .thenReturn(new Need(5, "Corn", 10.97, 100, NeedType.ITEM_DONATION, "moss inc"));
        when(mockDao.updateProposal(proposal)).thenReturn(proposal);

        Proposal result = service.approveProposal(1);

        assertEquals("approved", result.getStatus());
        verify(mockDao).updateProposal(proposal);

        // the need handed to createNeed should carry the proposal's own values
        ArgumentCaptor<Need> newNeed = ArgumentCaptor.forClass(Need.class);
        verify(mockNeedService).createNeed(newNeed.capture());
        assertEquals("Corn", newNeed.getValue().getName());
        assertEquals(10.97, newNeed.getValue().getCost());
        assertEquals(100, newNeed.getValue().getQuantity());
        assertEquals(NeedType.ITEM_DONATION, newNeed.getValue().getType());
        assertEquals("moss inc", newNeed.getValue().getOrganization());
    }

    // ---------- rejectProposal ----------

    @Test
    void testRejectProposalNotFound() throws IOException {
        when(mockDao.getProposalById(99)).thenReturn(null);

        assertNull(service.rejectProposal(99));
        verify(mockDao, never()).updateProposal(any(Proposal.class));
    }

    @Test
    void testRejectProposalAlreadyDecided() throws IOException {
        Proposal approved = pendingProposal();
        approved.setStatus("approved");
        when(mockDao.getProposalById(1)).thenReturn(approved);

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> service.rejectProposal(1));

        assertTrue(e.getMessage().contains("already been approved"));
    }

    @Test
    void testRejectProposalSuccess() throws IOException {
        Proposal proposal = pendingProposal();
        when(mockDao.getProposalById(1)).thenReturn(proposal);
        when(mockDao.updateProposal(proposal)).thenReturn(proposal);

        Proposal result = service.rejectProposal(1);

        assertEquals("rejected", result.getStatus());
        verify(mockDao).updateProposal(proposal);
    }

    // ---------- voteUpdate ----------

    @Test
    void testVoteUpdateProposalNotFound() throws IOException {
        when(mockDao.getProposalById(99)).thenReturn(null);

        assertNull(service.voteUpdate(99, "alice", 1));
    }

    /**
     * Voting is only allowed while a proposal is still pending.
     */
    @Test
    void testVoteUpdateNotPending() throws IOException {
        Proposal approved = pendingProposal();
        approved.setStatus("approved");
        when(mockDao.getProposalById(1)).thenReturn(approved);

        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> service.voteUpdate(1, "alice", 1));

        assertTrue(e.getMessage().contains("voting is only allowed while pending"));
    }

    /**
     * A user who hasn't voted yet gets their vote recorded.
     */
    @Test
    void testVoteUpdateFirstVoteIsRecorded() throws IOException {
        Proposal proposal = pendingProposal();
        when(mockDao.getProposalById(1)).thenReturn(proposal);
        when(mockDao.updateProposal(proposal)).thenReturn(proposal);

        service.voteUpdate(1, "alice", 1);

        assertEquals(1, proposal.getUserVoteStatus("alice"));
        verify(mockDao).updateProposal(proposal);
    }

    /**
     * Casting the same vote twice removes it, so the button acts as a toggle.
     */
    @Test
    void testVoteUpdateSameVoteRemovesIt() throws IOException {
        Map<String, Integer> votes = new HashMap<>();
        votes.put("alice", 1);
        Proposal proposal = new Proposal(1, "Corn", 10.97, 100, "ITEM_DONATION",
            "helper", "moss inc", votes, "pending");
        when(mockDao.getProposalById(1)).thenReturn(proposal);
        when(mockDao.updateProposal(proposal)).thenReturn(proposal);

        service.voteUpdate(1, "alice", 1);

        assertNull(proposal.getUserVoteStatus("alice"));
        assertFalse(proposal.getAllVotes().containsKey("alice"));
    }

    /**
     * Voting the other way replaces the existing vote instead of stacking.
     */
    @Test
    void testVoteUpdateOppositeVoteFlipsIt() throws IOException {
        Map<String, Integer> votes = new HashMap<>();
        votes.put("alice", 1);
        Proposal proposal = new Proposal(1, "Corn", 10.97, 100, "ITEM_DONATION",
            "helper", "moss inc", votes, "pending");
        when(mockDao.getProposalById(1)).thenReturn(proposal);
        when(mockDao.updateProposal(proposal)).thenReturn(proposal);

        service.voteUpdate(1, "alice", -1);

        assertEquals(-1, proposal.getUserVoteStatus("alice"));
        assertEquals(1, proposal.getAllVotes().size());
    }
}
