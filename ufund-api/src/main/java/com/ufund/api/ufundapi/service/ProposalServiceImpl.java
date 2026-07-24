package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.dao.ProposalDAO;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Proposal;

@Service
public class ProposalServiceImpl implements ProposalService {
    private ProposalDAO proposalDao;
    private NeedService needService;

    /**
     * Creates a ProposalServiceImpl with the provided {@link ProposalDAO}.
     *
     * @param proposalDao the {@link ProposalDAO} to use for data access
     */
    public ProposalServiceImpl(ProposalDAO proposalDao, NeedService needService) {
        this.proposalDao = proposalDao;
        this.needService = needService;
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
    public Proposal getProposalById(int id) throws IOException{
        return proposalDao.getProposalById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Proposal createProposal(Proposal newProposal) throws IOException {
		newProposal.setStatus("pending");
        return proposalDao.createProposal(newProposal);
    }

    public Proposal deleteProposal(int id) throws IOException {
        Proposal deletedProposal = proposalDao.getProposalById(id);
        if (deletedProposal != null && proposalDao.deleteProposal(id)) {
            return deletedProposal;
        } else {
            return null;
        }
    }

    public Proposal approveProposal(int id) throws IOException {
        Proposal proposal = proposalDao.getProposalById(id);
        if (proposal == null) { return null;}

        Need need = new Need(0, proposal.getName(), proposal.getCost(), proposal.getQuantity(), proposal.getType());

		boolean needExists = needService.getAllNeeds().stream().
			anyMatch(n -> n.getName().equalsIgnoreCase(proposal.getName()));

		if (needExists) {
			throw new IllegalArgumentException("Need name already exist");
		}
        Need createNeed = needService.createNeed((need));
        if (createNeed == null) {
            throw new IllegalStateException("creation failed");
        }

        proposal.setStatus("approved");
        return proposalDao.updateProposal(proposal);
    }

    public Proposal updateProposal(Proposal updProposal) throws IOException {

        boolean needExists = needService.getAllNeeds().stream()
            .anyMatch(n -> n.getName().equalsIgnoreCase(updProposal.getName()) &&
                        n.getCost() == updProposal.getCost() &&
                        n.getQuantity() == updProposal.getQuantity() &&
                        n.getType().equalsIgnoreCase(updProposal.getType()));
        if (needExists) {
            throw new IllegalArgumentException("Proposal can't be pending, matching Need already exist");
        }
        
        updProposal.setStatus("pending");
        return proposalDao.updateProposal(updProposal);
    }


    public Proposal rejectProposal(int id) throws IOException {
        Proposal proposal = proposalDao.getProposalById(id);
        if (proposal == null) return null;
            if ("rejected".equalsIgnoreCase(proposal.getStatus())) {
                    throw new IllegalArgumentException("Proposal is already rejected");
                }
            boolean needExists = needService.getAllNeeds().stream()
                .anyMatch(n -> n.getName().equalsIgnoreCase(proposal.getName()) &&
                            n.getCost() == proposal.getCost() &&
                            n.getQuantity() == proposal.getQuantity() &&
                            n.getType().equalsIgnoreCase(proposal.getType()));
            if (needExists) {
                throw new IllegalArgumentException("Proposal can't be rejected, matching Need already exist");
            }


        proposal.setStatus("rejected");

        return proposalDao.updateProposal(proposal);
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
