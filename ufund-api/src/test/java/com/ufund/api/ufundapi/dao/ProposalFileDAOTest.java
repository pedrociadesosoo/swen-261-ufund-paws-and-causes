package com.ufund.api.ufundapi.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Proposal;

@Tag("Persistence-tier")
class ProposalFileDAOTest {

    private ProposalFileDAO proposalFileDAO;
    private ObjectMapper mockObjectMapper;

    /**
     * Before each test we inject a mock ObjectMapper so the DAO never touches
     * the real proposals.json. Reads come from sampleProposals() and writes
     * quietly do nothing.
     */
    @BeforeEach
    void setupProposalFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.readValue(any(File.class), eq(Proposal[].class)))
            .thenReturn(sampleProposals());
        proposalFileDAO = new ProposalFileDAO("testfile.txt", mockObjectMapper);
    }

    /**
     * Three proposals from three different users, one in each status. The
     * usernames differ because createProposal blocks a user who already has a
     * pending proposal.
     */
    private Proposal[] sampleProposals() {
        return new Proposal[] {
            new Proposal(1, "Corn", 10.97, 100, "ITEM_DONATION", "alice", "moss inc", new HashMap<>(), "pending"),
            new Proposal(2, "Rice", 1.99, 50, "ITEM_DONATION", "bob", "moss inc", new HashMap<>(), "approved"),
            new Proposal(3, "Blankets", 15.00, 25, "ITEM_DONATION", "carol", "moss inc", new HashMap<>(), "rejected")
        };
    }

    /** Builds a DAO backed by an empty file, for the no-proposals cases. */
    private ProposalFileDAO emptyDAO() throws IOException {
        ObjectMapper emptyMapper = mock(ObjectMapper.class);
        when(emptyMapper.readValue(any(File.class), eq(Proposal[].class)))
            .thenReturn(new Proposal[0]);
        return new ProposalFileDAO("testfile.txt", emptyMapper);
    }

    // ---------- reads ----------

    @Test
    void testGetAllProposalsReturnsAll() {
        List<Proposal> result = proposalFileDAO.getAllProposals();

        assertEquals(3, result.size());
        assertEquals("Corn", result.get(0).getName());
        assertEquals("Rice", result.get(1).getName());
        assertEquals("Blankets", result.get(2).getName());
    }

    @Test
    void testGetAllProposalsWhenFileEmpty() throws IOException {
        assertTrue(emptyDAO().getAllProposals().isEmpty());
    }

    @Test
    void testGetProposalByIdFound() {
        Proposal result = proposalFileDAO.getProposalById(2);

        assertNotNull(result);
        assertEquals("Rice", result.getName());
        assertEquals("bob", result.getUsername());
    }

    @Test
    void testGetProposalByIdNotFound() {
        assertNull(proposalFileDAO.getProposalById(99));
    }

    @Test
    void testFindProposalsMatch() {
        List<Proposal> result = proposalFileDAO.findProposals("Corn");

        assertEquals(1, result.size());
        assertEquals("Corn", result.get(0).getName());
    }

    /** The search lowercases both sides, so "corn" still finds "Corn". */
    @Test
    void testFindProposalsIsCaseInsensitive() {
        List<Proposal> result = proposalFileDAO.findProposals("corn");

        assertEquals(1, result.size());
        assertEquals("Corn", result.get(0).getName());
    }

    @Test
    void testFindProposalsMultipleMatches() {
        // "Corn" and "Rice" both contain a c; "Blankets" doesn't
        List<Proposal> result = proposalFileDAO.findProposals("c");

        assertEquals(2, result.size());
    }

    @Test
    void testFindProposalsNoMatch() {
        assertTrue(proposalFileDAO.findProposals("xyz").isEmpty());
    }

    // ---------- createProposal ----------

    /**
     * The highest existing id is 3, so a new proposal should come back as 4.
     */
    @Test
    void testCreateProposalAssignsNextId() throws IOException {
        Proposal created = proposalFileDAO.createProposal(
            new Proposal(0, "Soap", 3.50, 10, "ITEM_DONATION", "dave", "moss inc", new HashMap<>(), "pending"));

        assertNotNull(created);
        assertEquals(4, created.getId());
        assertEquals("Soap", created.getName());
        assertEquals(4, proposalFileDAO.getAllProposals().size());
    }

    @Test
    void testCreateProposalOnEmptyFileStartsAtOne() throws IOException {
        Proposal created = emptyDAO().createProposal(
            new Proposal(0, "Soap", 3.50, 10, "ITEM_DONATION", "dave", "moss inc", new HashMap<>(), "pending"));

        assertNotNull(created);
        assertEquals(1, created.getId());
    }

    /**
     * alice already has a pending proposal, so a second one is refused.
     */
    @Test
    void testCreateProposalRejectsSecondPendingFromSameUser() throws IOException {
        Proposal created = proposalFileDAO.createProposal(
            new Proposal(0, "Soap", 3.50, 10, "ITEM_DONATION", "alice", "moss inc", new HashMap<>(), "pending"));

        assertNull(created);
        assertEquals(3, proposalFileDAO.getAllProposals().size());
    }

    /**
     * bob's only proposal was approved, not pending, so he can submit again.
     */
    @Test
    void testCreateProposalAllowedWhenUsersOnlyProposalIsDecided() throws IOException {
        Proposal created = proposalFileDAO.createProposal(
            new Proposal(0, "Soap", 3.50, 10, "ITEM_DONATION", "bob", "moss inc", new HashMap<>(), "pending"));

        assertNotNull(created);
        assertEquals(4, created.getId());
    }

    // ---------- updateProposal ----------

    @Test
    void testUpdateProposalCopiesFields() throws IOException {
        Proposal edits = new Proposal(1, "Corn Meal", 12.00, 150, "MONETARY",
            "alice", "new org", new HashMap<>(), "approved");

        Proposal updated = proposalFileDAO.updateProposal(edits);

        assertNotNull(updated);
        assertEquals("Corn Meal", updated.getName());
        assertEquals(12.00, updated.getCost());
        assertEquals(150, updated.getQuantity());
        assertEquals("MONETARY", updated.getType());
        assertEquals("approved", updated.getStatus());
        assertEquals("new org", updated.getOrganization());
    }

    @Test
    void testUpdateProposalUnknownId() throws IOException {
        Proposal edits = new Proposal(99, "Ghost", 1.00, 1, "ITEM_DONATION",
            "nobody", "moss inc", new HashMap<>(), "pending");

        assertNull(proposalFileDAO.updateProposal(edits));
    }

    // ---------- deleteProposal ----------

    @Test
    void testDeleteProposalExisting() throws IOException {
        assertTrue(proposalFileDAO.deleteProposal(1));

        assertEquals(2, proposalFileDAO.getAllProposals().size());
        assertNull(proposalFileDAO.getProposalById(1));
    }

    @Test
    void testDeleteProposalUnknownId() throws IOException {
        assertFalse(proposalFileDAO.deleteProposal(99));
        assertEquals(3, proposalFileDAO.getAllProposals().size());
    }

    // ---------- save failure ----------

    /**
     * If writing the file fails, the IOException should reach the caller
     * rather than being swallowed.
     */
    @Test
    void testSaveFailurePropagates() throws IOException {
        doThrow(new IOException("write failed"))
            .when(mockObjectMapper).writeValue(any(File.class), any(Object.class));

        assertThrows(IOException.class, () -> proposalFileDAO.deleteProposal(1));
    }

    /**
     * A failed write shouldn't leave memory out of step with the file, so a
     * deleted proposal is put back.
     */
    @Test
    void testDeleteRollsBackWhenSaveFails() throws IOException {
        doThrow(new IOException("write failed"))
            .when(mockObjectMapper).writeValue(any(File.class), any(Object.class));

        assertThrows(IOException.class, () -> proposalFileDAO.deleteProposal(1));

        assertNotNull(proposalFileDAO.getProposalById(1));
        assertEquals(3, proposalFileDAO.getAllProposals().size());
    }

    /** Likewise, a proposal that failed to save shouldn't linger in memory. */
    @Test
    void testCreateRollsBackWhenSaveFails() throws IOException {
        doThrow(new IOException("write failed"))
            .when(mockObjectMapper).writeValue(any(File.class), any(Object.class));

        assertThrows(IOException.class, () -> proposalFileDAO.createProposal(
            new Proposal(0, "Soap", 3.50, 10, "ITEM_DONATION", "dave", "moss inc", new HashMap<>(), "pending")));

        assertEquals(3, proposalFileDAO.getAllProposals().size());
        assertNull(proposalFileDAO.getProposalById(4));
    }

    /** And a failed edit leaves the original values in place. */
    @Test
    void testUpdateRollsBackWhenSaveFails() throws IOException {
        doThrow(new IOException("write failed"))
            .when(mockObjectMapper).writeValue(any(File.class), any(Object.class));

        Proposal edits = new Proposal(1, "Corn Meal", 12.00, 150, "MONETARY",
            "alice", "new org", new HashMap<>(), "approved");

        assertThrows(IOException.class, () -> proposalFileDAO.updateProposal(edits));

        Proposal unchanged = proposalFileDAO.getProposalById(1);
        assertEquals("Corn", unchanged.getName());
        assertEquals(10.97, unchanged.getCost());
        assertEquals(100, unchanged.getQuantity());
        assertEquals("ITEM_DONATION", unchanged.getType());
        assertEquals("pending", unchanged.getStatus());
        assertEquals("moss inc", unchanged.getOrganization());
    }
}
