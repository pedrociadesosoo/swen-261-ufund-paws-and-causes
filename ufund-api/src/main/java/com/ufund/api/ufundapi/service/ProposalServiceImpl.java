package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.ProposalDAO;
import com.ufund.api.ufundapi.model.Proposal;

@Service
public class ProposalServiceImpl implements ProposalService {
    private ProposalDAO proposalDao;

    /**
     * Creates a ProposalServiceImpl with the provided {@link ProposalDAO}.
     *
     * @param proposalDao the {@link ProposalDAO} to use for data access
     */
    public ProposalServiceImpl(ProposalDAO proposalDao) {
        this.proposalDao = proposalDao;
    }

    /**
     * {@inheritDoc}
     */
    public List<Proposal> getAllProposals() {
        return proposalDao.getAllProposals();
    }

    /**
     * {@inheritDoc}
     */
    public List<Proposal> findProposals(String pattern) {
        return proposalDao.findProposals(pattern);
    }

    /**
     * {@inheritDoc}
     */
    public Proposal getProposalById(int id) {
        return proposalDao.getProposalById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Proposal createProposal(Proposal newProposal) throws IOException {
        return proposalDao.createProposal(newProposal);
    }

    /**
     * {@inheritDoc}
     */
    public Proposal voteUpdate(int proposalId, String username, int vote) throws IOException {
        Proposal proposal = proposalDao.getProposalById(proposalId);
        if (proposal == null)
            return null;

        Integer current = proposal.getUserVoteStatus(username);
        if (current == null)            // user hasn't voted yet -> record it
            proposal.getAllVotes().put(username, vote);
        else if (current == vote)       // same vote again -> remove it (unvote)
            proposal.getAllVotes().remove(username);
        else                            // different vote -> replace it (flip)
            proposal.getAllVotes().put(username, vote);

        return proposalDao.updateProposal(proposal);
    }
}
