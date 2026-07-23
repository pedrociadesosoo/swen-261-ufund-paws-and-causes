package com.ufund.api.ufundapi.dao;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Proposal;

public interface ProposalDAO{
	
    List<Proposal> getAllProposals();
    List<Proposal> findProposals(String containsText);
    

    Proposal getProposalById(int id);
    /**
     * Creates a new {@linkplain Proposal proposal}
     * @param proposal {@link Proposal proposal} the proposal to be created
     * @return new {@link Proposal proposal} if successful, null if otherwise
     * @throws IOException if an issue with storage occurs
     */
    Proposal createProposal(Proposal proposal) throws IOException;

    Proposal updateProposal(Proposal proposal) throws IOException;


    /**
     * Deletes {@linkplain Proposal proposal} with the provided id
     * @param id if of the {@link Proposal proposal} to find and delete
     * @return true if successful, false if otherwise
     * @throws IOException if an issue with storage access occurs
     */
    boolean deleteProposal(int id) throws IOException;
}
