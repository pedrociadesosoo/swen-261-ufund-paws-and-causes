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

        Need need = new Need(id, proposal.getName(), proposal.getCost(), proposal.getQuantity(), proposal.getType());

        needService.createNeed(need);

        proposalDao.deleteProposal(id);

        return proposal;
    }

    public Proposal updateProposal(Proposal updProposal) throws IOException {
        return proposalDao.updateProposal(updProposal);
    }
    
 
}
