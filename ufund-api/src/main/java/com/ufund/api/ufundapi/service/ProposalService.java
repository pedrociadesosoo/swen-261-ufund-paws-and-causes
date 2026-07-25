package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Proposal;


public interface ProposalService{
 
	List<Proposal> getAllProposals();

	List<Proposal> findProposals(String pattern);

	Proposal getProposalById(int id) throws IOException;
	
	//My thought on this one is that when a user votes on a proposal
	//before getting updated the request is sent to the back-end
	// with the ID of the proposal being voted on (check if that exists)
	// the username, and what the vote is
	// 
	// if the proposal exists, check if user has voted on it before:
	// 
	// 	if they have not, add their name to the list of votes with their cast
	// 
	// 	if they have, then decide what to do based on how the new vote request
	// 	compares to the old one in the system:
	// 		
	// 		if the new and old vote match (Ex: they voted yes before and
	// 		this is another request to add a yes vote) then remove their
	// 		vote from the system (gives the ability to remove a vote entirely)
	//
	// 		if the new and old vote are different, replace the old vote with
	// 		the new (I.E. yes to no, no to yes)
	//
	/**
	 * Records a vote from a user on a proposal, toggling per the rules: a
	 * first-time vote is recorded; an identical repeat vote is removed
	 * (unvote); a differing vote replaces the previous one (flip).
	 *
	 * @param proposalId the id of the {@link Proposal proposal} being voted on
	 * @param username the user casting the vote
	 * @param vote the vote value (+1 or -1)
	 * @return the updated {@link Proposal proposal}, or {@code null} if no
	 *         proposal has that id (the controller maps null to HTTP 404)
	 * @throws IOException if an issue with storage access occurs
	 */
	Proposal voteUpdate(int proposalId, String username, int vote) throws IOException;
	/**
	 * Handles a request to create a new proposal.
	 *
	 * @param newProposal the {@link Proposal proposal} to create
	 * @return the created proposal, or {@code null} if the user already has a
	 *         pending proposal (the controller maps null to HTTP 409 CONFLICT)
	 * @throws IOException if an issue with storage access occurs
	 */
	Proposal createProposal(Proposal newProposal) throws IOException;

	Proposal deleteProposal(int id) throws IOException;

	Proposal approveProposal(int id) throws IOException;

	Proposal updateProposal(Proposal proposal) throws IOException;

	Proposal rejectProposal(int id) throws IOException;

}
